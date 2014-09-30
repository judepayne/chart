# chart

Real-time charts for Clojure

## Usage

add the following to the dependencies in your project.clj file

    [live-chart "0.1.1"]

In a src file, require chart like this:

    ..(:require [live-chart :as c])..

Define some functions that take no args:  
e.g.

    (defn rand1 [] (rand 1))

(maybe your functions will deref an atom or similar)

then:

    (show (time-chart [rand1 rand1]) :title "test random funcs")

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.