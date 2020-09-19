package de.jnmeyr.mandelbrot

import scala.annotation.tailrec

object MandelBrot {

  def apply(c: Complex)
           (implicit mandelContext: MandelContext): Option[Int] = {
    @tailrec
    def z(n: Int, zn: Complex): Option[Int] = {
      if (mandelContext.diverges(zn.magnitude))
        Some(n)
      else if (mandelContext.converges(n))
        None
      else
        z(n + 1, zn.square + c)
    }

    z(0, Complex.zero)
  }

}
