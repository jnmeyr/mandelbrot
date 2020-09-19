package de.jnmeyr.mandelbrot

import eu.timepit.refined.auto._
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class SplitSpec
  extends AnyWordSpec
    with should.Matchers {

  "into" should {
    "split correctly" in {
      Split(Seq(), 1) should be(Seq())
      Split(Seq(), 2) should be(Seq())
      Split(Seq(), 3) should be(Seq())

      Split(Seq(1), 1) should be(Seq(Seq(1)))
      Split(Seq(1), 2) should be(Seq(Seq(1)))
      Split(Seq(1), 3) should be(Seq(Seq(1)))

      Split(Seq(1, 2), 1) should be(Seq(Seq(1, 2)))
      Split(Seq(1, 2), 2) should be(Seq(Seq(1), Seq(2)))
      Split(Seq(1, 2), 3) should be(Seq(Seq(1), Seq(2)))

      Split(Seq(1, 2, 3), 1) should be(Seq(Seq(1, 2, 3)))
      Split(Seq(1, 2, 3), 2) should be(Seq(Seq(1, 2), Seq(3)))
      Split(Seq(1, 2, 3), 3) should be(Seq(Seq(1), Seq(2), Seq(3)))

      Split(Seq(1, 2, 3, 4), 1) should be(Seq(Seq(1, 2, 3, 4)))
      Split(Seq(1, 2, 3, 4), 2) should be(Seq(Seq(1, 2), Seq(3, 4)))
      Split(Seq(1, 2, 3, 4), 3) should be(Seq(Seq(1, 2), Seq(3), Seq(4)))

      Split(Seq(1, 2, 3, 4, 5), 1) should be(Seq(Seq(1, 2, 3, 4, 5)))
      Split(Seq(1, 2, 3, 4, 5), 2) should be(Seq(Seq(1, 2, 3), Seq(4, 5)))
      Split(Seq(1, 2, 3, 4, 5), 3) should be(Seq(Seq(1, 2), Seq(3, 4), Seq(5)))
    }
  }

}
