package de.jnmeyr.mandelbrot

import java.awt.image.{BufferedImage, RenderedImage}
import java.io.File

import de.jnmeyr.mandelbrot.MandelImage.RGB
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean.And
import eu.timepit.refined.numeric.{GreaterEqual, Less}
import eu.timepit.refined.refineV
import javax.imageio.ImageIO
import shapeless.{Witness => W}

class MandelImage private(image: BufferedImage) {

  def set(x: Int, y: Int, rgb: RGB): Unit =
    image.asInstanceOf[BufferedImage].setRGB(x, y, rgb.argb)

  def write(name: String): Unit =
    ImageIO.write(image.asInstanceOf[RenderedImage], "png", new File(s"$name.png"))

}

object MandelImage {

  def apply(frame: MandelFrame,
            solution: Solution,
            name: String)
           (implicit mandelContext: MandelContext): Unit = {
    val image = new MandelImage(new BufferedImage(frame.width, frame.height, BufferedImage.TYPE_INT_ARGB))
    solution.foreach {
      case After(x, y, c, nOpt) =>
        image.set(x, y, Colorize(Colorize.white)(c, nOpt))
    }
    image.write(name)
  }

  type Byte = GreaterEqual[W.`0`.T] And Less[W.`256`.T]
  type Channel = Int Refined Byte

  object Channel {

    def apply(n: Int): Channel =
      refineV[Byte](n % 256).getOrElse(0)

  }

  case class RGB(r: Channel = 0,
                 g: Channel = 0,
                 b: Channel = 0,
                 a: Channel = 255) {

    lazy val argb: Int = (a << 24) + (r << 16) + (g << 8) + b

    lazy val rgba: Int = (r << 24) + (g << 16) + (b << 8) + a

  }

  object RGB {

    val black: RGB = RGB()

  }

  object Colorize {

    def apply(color: Channel => RGB)
             (c: Complex,
              nOpt: Option[Int])
             (implicit mandelContext: MandelContext): RGB =
      nOpt.fold(RGB.black)(
        n => if (mandelContext.diverges(c.magnitude)) RGB.black else color(Channel(n))
      )

    def red(n: Channel): RGB = RGB(r = n)

    def green(n: Channel): RGB = RGB(g = n)

    def blue(n: Channel): RGB = RGB(b = n)

    def purple(n: Channel): RGB = RGB(r = n, b = n)

    def white(n: Channel): RGB = RGB(r = n, g = n, b = n)

  }

}
