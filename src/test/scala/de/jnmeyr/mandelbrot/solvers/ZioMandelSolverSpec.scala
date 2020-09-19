package de.jnmeyr.mandelbrot.solvers

import de.jnmeyr.mandelbrot.solvers.MandelSolver.Parallelism
import eu.timepit.refined.auto._
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class ZioMandelSolverSpec
  extends AnyWordSpec
    with should.Matchers
    with MandelSolverBehaviour {

  import MandelSolverBehaviour.Implicits._

  "A zio mandel solver" when {
    "using naive parallelism" should {
      behave like mandelSolver(ZioMandelSolver(Parallelism.Naive))
    }
    "using a parallelism level of 1" should {
      behave like mandelSolver(ZioMandelSolver(Parallelism.Smart(1)))
    }
    "using a parallelism level of 4" should {
      behave like mandelSolver(ZioMandelSolver(Parallelism.Smart(4)))
    }
    "using a parallelism level of 512" should {
      behave like mandelSolver(ZioMandelSolver(Parallelism.Smart(512)))
    }
  }

}
