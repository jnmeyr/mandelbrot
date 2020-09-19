package de.jnmeyr.mandelbrot.solvers
import eu.timepit.refined.auto._
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class ActorMandelSolverSpec
  extends AnyWordSpec
    with should.Matchers
    with MandelSolverBehaviour {

  import MandelSolverBehaviour.Implicits._

  "An actor mandel solver" when {
    "using a parallelism level of 1" should {
      behave like mandelSolver(new ActorMandelSolver(1))
    }
    "using a parallelism level of 4" should {
      behave like mandelSolver(new ActorMandelSolver(4))
    }
    "using a parallelism level of 512" should {
      behave like mandelSolver(new ActorMandelSolver(512))
    }
  }

}
