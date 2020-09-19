package de.jnmeyr.mandelbrot.solvers.benchmarks

import de.jnmeyr.mandelbrot.solvers.EffectMandelSolver
import de.jnmeyr.mandelbrot.solvers.benchmarks.MandelSolverBenchmark.Implicits._
import eu.timepit.refined.auto._
import org.openjdk.jmh.annotations._

@BenchmarkMode(Array(Mode.AverageTime))
@Fork(1)
@State(Scope.Thread)
@Threads(1)
class Effect256MandelSolverBenchmark extends MandelSolverBenchmark(new EffectMandelSolver.Smart(256))
