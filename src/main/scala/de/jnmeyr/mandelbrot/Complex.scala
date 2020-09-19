package de.jnmeyr.mandelbrot

import scala.math.sqrt

case class Complex(r: Double, i: Double) {

  def +(c: Complex): Complex = Complex(r + c.r, i + c.i)

  lazy val square: Complex = Complex(r * r - i * i, r * i + i * r)

  lazy val magnitude: Double = sqrt((r * r) + (i * i))

}

object Complex {

  val zero: Complex = Complex(0, 0)

}
