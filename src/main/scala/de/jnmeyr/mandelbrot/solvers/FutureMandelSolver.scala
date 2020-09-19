package de.jnmeyr.mandelbrot.solvers

import de.jnmeyr.mandelbrot.solvers.MandelSolver.Parallelism
import de.jnmeyr.mandelbrot.{MandelContext, Problem, Solvable, _}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps

object FutureMandelSolver {

  class Naive()
             (implicit executionContext: ExecutionContext,
              mandelContext: MandelContext)
    extends MandelSolver {

    import Solvable.Instances._

    override def apply(problem: Problem): Solution = {
      val future = Future.traverse(problem) { before =>
        Future {
          Solvable[Before, After].solve(before)
        }
      }
      Await.result(future, timeout)
    }

    override val toString: String = "future"

  }

  class Smart(parallelismLevel: Parallelism.Level)
             (implicit executionContext: ExecutionContext,
              mandelContext: MandelContext)
    extends MandelSolver {

    import Solvable.Instances._

    override def apply(problem: Problem): Solution = {
      val problems = Split(problem, parallelismLevel)
      val future = Future.traverse(problems) { problem =>
        Future {
          Solvable[Problem, Solution].solve(problem)
        }
      }
      Await.result(future, timeout).flatten
    }

    override val toString: String = s"${parallelismLevel}future${if (parallelismLevel.value == 1) "" else "s"}"

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
