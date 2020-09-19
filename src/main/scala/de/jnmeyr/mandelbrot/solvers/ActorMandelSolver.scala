package de.jnmeyr.mandelbrot.solvers

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import akka.util.Timeout
import de.jnmeyr.mandelbrot._
import de.jnmeyr.mandelbrot.solvers.ActorMandelSolver.{Solve, Solved}
import de.jnmeyr.mandelbrot.solvers.MandelSolver.Parallelism

import scala.concurrent.{Await, ExecutionContext}
import scala.language.postfixOps

class WorkerActor()
                 (implicit mandelContext: MandelContext)
  extends Actor
    with ActorLogging {

  import Solvable.Instances._

  override def receive: Receive = {
    case Solve(problem) =>
      val solution = Solvable[Problem, Solution].solve(problem)
      sender() ! Solved(solution)
  }

}

class RouterActor(parallelismLevel: Int,
                  mandelContext: MandelContext)
  extends Actor
    with ActorLogging {

  import RouterActor._

  private val router: Router = {
    val workers = Vector.fill(parallelismLevel) {
      val receiver = context.actorOf(Props(classOf[WorkerActor], mandelContext))
      context.watch(receiver)
      ActorRefRoutee(receiver)
    }
    Router(RoundRobinRoutingLogic(), workers)
  }

  private def idle: Receive = {
    case Solve(problem) =>
      val problems = Split(problem, Parallelism.Level(parallelismLevel))
      problems.foreach { problem =>
        router.route(Solve(problem), self)
      }
      context.become(work(Work(sender(), problems)))
  }

  private def work(previousWork: Work): Receive = {
    case Solved(solution) =>
      val nextWork = previousWork(solution)
      nextWork match {
        case Work.Done(receiver, solutions) =>
          receiver ! Solved(solutions)
          context.unbecome()
        case _ =>
          context.become(work(nextWork), discardOld = true)
      }
  }

  override def receive: Receive = idle

}

object RouterActor {

  case class Work private(receiver: ActorRef, unsolvedProblems: Int, solvedSolutions: Solution) {

    def apply(solution: Solution): Work =
      copy(unsolvedProblems = unsolvedProblems - 1, solvedSolutions = solvedSolutions ++ solution)

  }

  object Work {

    def apply(receiver: ActorRef, problems: Seq[Problem]): Work =
      Work(receiver, unsolvedProblems = problems.size, solvedSolutions = Seq.empty)

    object Done {

      def unapply(work: Work): Option[(ActorRef, Solution)] =
        if (work.unsolvedProblems <= 0) Some(work.receiver -> work.solvedSolutions) else None

    }

  }

}

class ActorMandelSolver(parallelismLevel: Parallelism.Level)
                       (implicit executionContext: ExecutionContext,
                        mandelContext: MandelContext)
  extends MandelSolver {

  import ActorMandelSolver._

  private var actorSystem: ActorSystem = _

  private var routerActor: ActorRef = _

  override def start(): Unit = {
    actorSystem = ActorSystem("system")
    routerActor = actorSystem.actorOf(Props(classOf[RouterActor], parallelismLevel.value, mandelContext), "router")
  }

  override def apply(problem: Problem): Solution = {
    Await.result[Solution](ask(routerActor, Solve(problem))(Timeout(timeout)).collect {
      case Solved(solution) =>
        solution
    }, timeout)
  }

  override def stop(): Unit = {
    Await.ready(actorSystem.terminate(), timeout)
  }

  override val toString: String = s"${parallelismLevel}actor${if (parallelismLevel.value == 1) "" else "s"}"

}

object ActorMandelSolver {

  case class Solve(problem: Problem)

  case class Solved(solution: Solution)

}
