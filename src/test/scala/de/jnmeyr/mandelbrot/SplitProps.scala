package de.jnmeyr.mandelbrot

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.scalacheck.numeric._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.matchers.should
import org.scalatest.propspec.AnyPropSpec

class SplitProps
  extends AnyPropSpec
    with should.Matchers {

  def valuesGen[T: Arbitrary]: Gen[Seq[T]] =
    Gen.listOf(arbitrary[T])

  def nonEmptyValuesGen[T: Arbitrary]: Gen[Seq[T]] =
    Gen.nonEmptyListOf(arbitrary[T])

  val intoGen: Gen[Int Refined Positive] =
    chooseRefinedNum(1, 5)

  property("into should neither forget nor conceive values") {
    forAll(valuesGen[Int], intoGen) {
      case (values, into) =>
        Split(values, into).flatten == values
    }
  }

  property("into should split evenly") {
    forAll(nonEmptyValuesGen[Int], intoGen) {
      case (values, into) =>
        val sizes = Split(values, into).map(_.size)
        Set(0, 1).contains(sizes.max - sizes.min)
    }
  }

  property("into should only split as much as requested") {
    forAll(valuesGen[Int], intoGen) {
      case (values, into) =>
        Split(values, into).length <= into
    }
  }

}
