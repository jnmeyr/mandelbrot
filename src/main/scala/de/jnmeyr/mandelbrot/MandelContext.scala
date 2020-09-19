package de.jnmeyr.mandelbrot

import de.jnmeyr.mandelbrot.MandelContext.{MaxMagnitude, MaxN}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.GreaterEqual
import shapeless.{Witness => W}

import scala.language.postfixOps

case class MandelContext(maxN: MaxN = 255,
                         maxMagnitude: MaxMagnitude = 2) {

  def converges(n: Int): Boolean =
    n >= maxN

  def diverges(magnitude: Double): Boolean =
    magnitude > maxMagnitude

}

object MandelContext {

  type MaxN = Int Refined GreaterEqual[W.`0`.T]

  type MaxMagnitude = Int Refined GreaterEqual[W.`0`.T]

}
