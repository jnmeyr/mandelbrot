package de.jnmeyr.mandelbrot.solvers

import de.jnmeyr.mandelbrot._
import de.jnmeyr.mandelbrot.solvers.MandelSolver.Parallelism
import zio._

object ZioMandelSolver {

  class Naive()
             (implicit mandelContext: MandelContext)
    extends MandelSolver {

    import Solvable.Instances._

    private val runtime = Runtime.default

    private def solve(problem: Problem): Task[Solution] = {
      def solve(before: Before): Task[After] = Task {
        Solvable[Before, After].solve(before)
      }

      Task.collectAllPar(problem.map(solve))
    }

    override def apply(problem: Problem): Solution = {
      runtime.unsafeRun(solve(problem))
    }

    override val toString: String = "zio"

  }

  class Smart(parallelismLevel: Parallelism.Level)
             (implicit mandelContext: MandelContext)
    extends MandelSolver {

    import Solvable.Instances._

    private val runtime = Runtime.default

    private def solve(problems: Seq[Problem]): Task[Solution] = {
      def solve(problem: Problem): Task[Solution] = Task {
        Solvable[Problem, Solution].solve(problem)
      }

      Task.collectAllPar(problems.map(solve)).map(_.flatten)
    }

    override def apply(problem: Problem): Solution = {
      val problems = Split(problem, parallelismLevel)
      runtime.unsafeRun(solve(problems))
    }

    override val toString: String = s"${parallelismLevel}zio${if (parallelismLevel.value == 1) "" else "s"}"

  }


  def apply(parallelism: Parallelism)
           (implicit mandelContext: MandelContext): MandelSolver = {
    parallelism match {
      case Parallelism.Naive => new Naive()
      case Parallelism.Smart(level) => new Smart(level)
    }
  }

}
