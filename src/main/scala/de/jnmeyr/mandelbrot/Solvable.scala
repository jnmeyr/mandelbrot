package de.jnmeyr.mandelbrot

sealed trait Solvable[A, B] {

  def solve(a: A): B

}

object Solvable {

  def apply[A, B](implicit solvable: Solvable[A, B]): Solvable[A, B] = solvable

  object Instances {

    implicit def complex2optionalInt(implicit mandelContext: MandelContext): Solvable[Complex, Option[Int]] = new Solvable[Complex, Option[Int]] {
      override def solve(c: Complex): Option[Int] =
        MandelBrot(c)
    }

    implicit def before2after(implicit solvable: Solvable[Complex, Option[Int]]): Solvable[Before, After] = new Solvable[Before, After] {
      override def solve(before: Before): After =
        After(before.x, before.y, before.c, solvable.solve(before.c))
    }

    implicit def problem2solution(implicit solvable: Solvable[Before, After]): Solvable[Problem, Solution] = new Solvable[Problem, Solution] {
      override def solve(problem: Problem): Solution =
        problem.map(solvable.solve)
    }

  }

}
