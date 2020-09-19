package de.jnmeyr.mandelbrot.solvers

import de.jnmeyr.mandelbrot.{Problem, Solution}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.refineV

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.language.postfixOps

trait MandelSolver {

  protected implicit val timeout: FiniteDuration = 1 minute

  def start(): Unit = {}

  def apply(problem: Problem): Solution

  def stop(): Unit = {}

  override val toString: String = "unknown"

}

object MandelSolver {

  sealed trait Parallelism

  object Parallelism {

    type Level = Int Refined Positive

    object Level {

      def apply(level: Int): Level =
        refineV[Positive](level).getOrElse(1)

    }

    case object Naive extends Parallelism

    case class Smart(level: Level) extends Parallelism

  }

  def apply[T](solver: MandelSolver)
              (apply: MandelSolver => T): T = {
    solver.start()
    try {
      apply(solver)
    } finally {
      solver.stop()
    }
  }

}
