name := "mandelbrot"
scalaVersion := "2.13.0"
version := "spfaez"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.25"
libraryDependencies += "dev.zio" %% "zio" % "1.0.1"
libraryDependencies += "eu.timepit" %% "refined" % "0.9.15"
libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.2.0"

libraryDependencies += "eu.timepit" %% "refined-scalacheck" % "0.9.15"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"
libraryDependencies += "org.scalatest" %% "scalatest-propspec" % "3.2.0" % "test"
libraryDependencies += "org.scalatest" %% "scalatest-wordspec" % "3.2.0" % "test"

enablePlugins(JmhPlugin)
