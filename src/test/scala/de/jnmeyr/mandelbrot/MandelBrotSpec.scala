package de.jnmeyr.mandelbrot

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers._

class MandelBrotSpec
  extends AnyWordSpec
    with should.Matchers {

  implicit val mandelContext: MandelContext = MandelContext()

  "MandelBrot" should {
    "converge" in {
      MandelBrot(Complex(0, 0)) should be(None)
      MandelBrot(Complex(-1, 0)) should be(None)
    }
    "diverge" in {
      MandelBrot(Complex(0, 2)) should contain(2)
      MandelBrot(Complex(-1, 1)) should contain(3)
      MandelBrot(Complex(-2, 1)) should contain(1)
    }
  }

}
