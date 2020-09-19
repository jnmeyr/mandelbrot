package de.jnmeyr.mandelbrot.solvers

import de.jnmeyr.mandelbrot._

import scala.collection.parallel.CollectionConverters._

class ParallelMandelSolver()
                          (implicit val mandelContext: MandelContext)
  extends MandelSolver {

  import Solvable.Instances._

  override def apply(problem: Problem): Solution =
    problem.par.map(Solvable[Before, After].solve).seq

  override val toString: String = "parallel"

}
