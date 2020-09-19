package de.jnmeyr.mandelbrot

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive

import scala.annotation.tailrec

object Split {

  def apply[T](values: Seq[T], into: Int Refined Positive): Seq[Seq[T]] = {
    val clean = values.size / into
    val dirty = values.size % into
    def take(index: Int): Int = clean + (if (index < dirty) 1 else 0)
    val takes = (0 until into).map(take).filter(_ > 0)

    @tailrec
    def split(values: Seq[T], takes: Seq[Int], splitValues: Seq[Seq[T]]): Seq[Seq[T]] = {
      takes.headOption match {
        case None =>
          splitValues
        case Some(take) =>
          val (head, tail) = values.splitAt(take)
          split(tail, takes.tail, splitValues :+ head)
      }
    }

    split(values, takes, Vector.empty)
  }

}
