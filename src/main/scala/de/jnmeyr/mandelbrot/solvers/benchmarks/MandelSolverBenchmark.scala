package de.jnmeyr.mandelbrot.solvers.benchmarks

import de.jnmeyr.mandelbrot.solvers.MandelSolver
import de.jnmeyr.mandelbrot.{MandelContext, MandelFrame, Problem}
import org.openjdk.jmh.annotations.{Benchmark, Setup, TearDown}
import org.openjdk.jmh.infra.Blackhole

import scala.concurrent.ExecutionContext

abstract class MandelSolverBenchmark(solver: MandelSolver) {

  import MandelSolverBenchmark._

  @Setup
  def setup(): Unit =
    solver.start()

  @Benchmark
  def benchmark(blackhole: Blackhole): Unit =
    blackhole.consume(solver(problem))

  @TearDown
  def tearDown(): Unit =
    solver.stop()

}

object MandelSolverBenchmark {

  object Implicits {

    implicit val executionContext: ExecutionContext = ExecutionContext.global
    implicit val mandelContext: MandelContext = MandelContext()

  }

  val problem: Problem = Problem(MandelFrame.forExtract(-2, 1, -1, 1, 1000))

}
