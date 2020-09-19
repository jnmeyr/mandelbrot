package de.jnmeyr.mandelbrot.solvers

import cats.Parallel
import cats.effect.IO._
import cats.effect.{ContextShift, IO, Sync, Timer}
import cats.implicits._
import de.jnmeyr.mandelbrot.solvers.MandelSolver.Parallelism
import de.jnmeyr.mandelbrot.{MandelContext, Problem, Solution, Solvable, _}

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

object EffectMandelSolver {

  class Naive()
             (implicit executionContext: ExecutionContext,
              mandelContext: MandelContext)
    extends MandelSolver {

    import Solvable.Instances._

    private implicit val contextShift: ContextShift[IO] = IO.contextShift(executionContext)
    private implicit val timer: Timer[IO] = IO.timer(executionContext)

    private def solve[F[_] : Sync : Parallel](problem: Problem): F[Solution] = {
      def solve(before: Before): F[After] = Sync[F].delay {
        Solvable[Before, After].solve(before)
      }

      problem.map(solve).toList.parSequence.map(_.toSeq)
    }

    override def apply(problem: Problem): Solution = {
      solve[IO](problem).unsafeRunSync()
    }

    override val toString: String = "effect"

  }

  class Smart(parallelismLevel: Parallelism.Level)
             (implicit executionContext: ExecutionContext,
              mandelContext: MandelContext)
    extends MandelSolver {

    import Solvable.Instances._

    private implicit val contextShift: ContextShift[IO] = IO.contextShift(executionContext)
    private implicit val timer: Timer[IO] = IO.timer(executionContext)

    private def solve[F[_] : Sync : Parallel](problems: Seq[Problem]): F[Solution] = {
      def solve(problem: Problem): F[Solution] = Sync[F].delay {
        Solvable[Problem, Solution].solve(problem)
      }

      problems.map(solve).toList.parSequence.map(_.flatten)
    }

    override def apply(problem: Problem): Solution = {
      val problems = Split(problem, parallelismLevel)
      solve[IO](problems).unsafeRunSync()
    }

    override val toString: String = s"${parallelismLevel}effect${if (parallelismLevel.value == 1) "" else "s"}"

  }

  def apply(parallelism: Parallelism)
           (implicit executionContext: ExecutionContext,
            mandelContext: MandelContext): MandelSolver = {
    parallelism match {
      case Parallelism.Naive => new Naive()
      case Parallelism.Smart(level) => new Smart(level)
    }
  }

}
