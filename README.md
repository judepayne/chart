# chart

Real-time charts for Clojure

## Usage

add the following to the dependencies in your project.clj file

    [chart "0.1.0-SNAPSHOT"]

In a src file, require chart like this:

    ..(:require [chart/core :as c])..

e.g.
Define some functions that take no args:

    (defn rand1 [] (rand 1))

(maybe your functions will deref an atom or similar)

then:

    (show (time-chart [rand1 rand1]) :title "test random funcs")

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.