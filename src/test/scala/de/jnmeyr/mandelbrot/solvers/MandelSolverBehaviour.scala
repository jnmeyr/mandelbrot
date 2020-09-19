package de.jnmeyr.mandelbrot.solvers

import de.jnmeyr.mandelbrot.{MandelContext, MandelFrame, Problem}
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.ExecutionContext

trait MandelSolverBehaviour { self: AnyWordSpec with should.Matchers =>

  import MandelSolverBehaviour._

  def mandelSolver(solver: MandelSolver): Unit = {
    "" should {
      "solve the problem" in {
        val actualSolution = MandelSolver(solver)(_ (problem))
        actualSolution should contain theSameElementsAs expectedSolution
      }
    }
  }

}

object MandelSolverBehaviour {

  object Implicits {

    implicit val executionContext: ExecutionContext = ExecutionContext.global
    implicit val mandelContext: MandelContext = MandelContext()

  }

  private val problem = Problem(MandelFrame.forExtract(-2, 1, -1, 1, 20))
  private val expectedSolution = MandelSolver(new SequentialMandelSolver()(Implicits.mandelContext))(_ (problem))

}
