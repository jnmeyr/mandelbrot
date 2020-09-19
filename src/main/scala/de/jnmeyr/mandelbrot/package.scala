package de.jnmeyr

package object mandelbrot {

  def measured[T](block: => T): (Long, T) = {
    val t0 = System.nanoTime()
    val result = block
    val t1 = System.nanoTime()
    (t1 - t0) / 1000000 -> result
  }

  case class Before(x: Int, y: Int, c: Complex)

  case class After(x: Int, y: Int, c: Complex, nOpt: Option[Int])

  type Problem = Seq[Before]

  object Problem {

    def apply(frame: MandelFrame): Problem = {
      for {
        x <- 0 until frame.width
        y <- 0 until frame.height
      } yield {
        Before(x, y, Complex(frame.left + frame.horizontal * x, frame.bottom + frame.vertical * y))
      }
    }

  }

  type Solution = Seq[After]

}
