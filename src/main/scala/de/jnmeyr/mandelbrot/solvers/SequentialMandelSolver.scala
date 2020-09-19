package de.jnmeyr.mandelbrot.solvers

import de.jnmeyr.mandelbrot.{MandelContext, Solvable, _}

class SequentialMandelSolver()
                            (implicit mandelContext: MandelContext)
  extends MandelSolver {

  import Solvable.Instances._

  override def apply(problem: Problem): Solution =
    Solvable[Problem, Solution].solve(problem)

  override val toString: String = "sequential"

}
