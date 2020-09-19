package de.jnmeyr.mandelbrot.solvers.benchmarks

import de.jnmeyr.mandelbrot.solvers.FutureMandelSolver
import de.jnmeyr.mandelbrot.solvers.MandelSolver.Parallelism
import de.jnmeyr.mandelbrot.solvers.benchmarks.MandelSolverBenchmark.Implicits._
import eu.timepit.refined.auto._
import org.openjdk.jmh.annotations._

@BenchmarkMode(Array(Mode.AverageTime))
@Fork(1)
@State(Scope.Thread)
@Threads(1)
class Future16384MandelSolverBenchmark extends MandelSolverBenchmark(FutureMandelSolver(Parallelism.Smart(16384)))
