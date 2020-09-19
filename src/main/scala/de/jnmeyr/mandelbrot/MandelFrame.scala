package de.jnmeyr.mandelbrot

case class MandelFrame private(left: Double,
                               right: Double,
                               bottom: Double,
                               top: Double,
                               width: Int,
                               height: Int) {

  val horizontal: Double = (right - left) / width
  val vertical: Double = (top - bottom) / height

  override val toString: String = s"[$left,$right,$bottom,$top]-$width-$height"

}

object MandelFrame {

  def forExtract(left: Double, right: Double, bottom: Double, top: Double, scaling: Int): MandelFrame = {
    val width: Int = (scaling * (right - left)).toInt
    val height: Int = (scaling * (top - bottom)).toInt

    MandelFrame(left, right, bottom, top, width, height)
  }

  def forResolution(x: Double, y: Double, width: Int, height: Int, scaling: Double): MandelFrame = {
    val dx = (scaling * width) / 2
    val left = x - dx
    val right = x + dx

    val dy = (scaling * height) / 2
    val bottom = y - dy
    val top = y + dy

    MandelFrame(left, right, bottom, top, width, height)
  }

}
