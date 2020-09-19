set terminal pngcairo
set output "benchmarks.png"

set logscale x 2
set xtics add ("" 0.5)
set xlabel 'parallelism level'
set format x "2^{%L}"

set ylabel 's/op'

plot 'benchmarks.akka.dat' title 'akka' with yerrorbars linecolor 1, \
     ''                    notitle      with lines linecolor 1, \
     'benchmarks.cats.dat' title 'cats' with yerrorbars linecolor 2, \
     ''                    notitle      with lines linecolor 2, \
     'benchmarks.futures.dat' title 'futures' with yerrorbars linecolor 3, \
     ''                       notitle         with lines linecolor 3, \
     'benchmarks.parallel.dat' title 'parallel' with yerrorbars linecolor 4, \
     ''                        notitle          with lines linecolor 4, \
     'benchmarks.sequential.dat' title 'sequential' with yerrorbars linecolor 5, \
     ''                          notitle            with lines linecolor 5, \
     'benchmarks.zio.dat' title 'zio' with yerrorbars linecolor 6, \
     ''                   notitle     with lines linecolor 6

