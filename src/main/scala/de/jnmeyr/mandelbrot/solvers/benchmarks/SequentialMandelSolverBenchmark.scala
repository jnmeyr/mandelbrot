package de.jnmeyr.mandelbrot.solvers.benchmarks

import de.jnmeyr.mandelbrot.solvers.SequentialMandelSolver
import de.jnmeyr.mandelbrot.solvers.benchmarks.MandelSolverBenchmark.Implicits._
import org.openjdk.jmh.annotations._

@BenchmarkMode(Array(Mode.AverageTime))
@Fork(1)
@State(Scope.Thread)
@Threads(1)
class SequentialMandelSolverBenchmark extends MandelSolverBenchmark(new SequentialMandelSolver())
