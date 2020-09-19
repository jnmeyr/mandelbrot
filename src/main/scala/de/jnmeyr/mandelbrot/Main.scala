package de.jnmeyr.mandelbrot

import de.jnmeyr.mandelbrot.solvers.MandelSolver.Parallelism
import de.jnmeyr.mandelbrot.solvers.{FutureMandelSolver, MandelSolver}
import eu.timepit.refined.auto._

import scala.concurrent.ExecutionContext

object Main {

  implicit val executionContext: ExecutionContext = ExecutionContext.global
  implicit val mandelContext: MandelContext = MandelContext()

  def main(arguments: Array[String]): Unit = {
    val frame = MandelFrame.forExtract(-2, 1, -1, 1, 200)
    val problem = Problem(frame)
    val (info, solution) = MandelSolver(FutureMandelSolver(Parallelism.Smart(16))) { solver =>
      val (millis, solution) = measured { solver(problem) }
      (s"$solver-$millis", solution)
    }
    MandelImage(frame, solution, s"mandelbrot-${System.currentTimeMillis}-$frame-$info")
  }

}
