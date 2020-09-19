package de.jnmeyr.mandelbrot.solvers

import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class ParallelMandelSolverSpec
  extends AnyWordSpec
    with should.Matchers
    with MandelSolverBehaviour {

  import MandelSolverBehaviour.Implicits._

  "A parallel mandel solver" should {
    behave like mandelSolver(new ParallelMandelSolver())
  }

}
