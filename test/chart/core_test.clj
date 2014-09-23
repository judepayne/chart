(ns chart.core-test
  (:require [clojure.test :refer :all]
            [chart.core :refer :all]))

(defn rand1 []
  (rand 1))

(show (time-chart [rand1 rand1]) :title "test random funcs")
