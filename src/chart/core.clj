(ns chart.core
  (:use clojure.core.matrix)
  (:import [javax.swing JComponent JLabel JPanel JFrame])
  (:import [java.awt Graphics2D Color GridLayout])
  (:import [java.awt.event ActionEvent ActionListener])
  (:import [java.awt.image BufferedImage])
  (:import [org.jfree.chart ChartPanel JFreeChart])
  (:require [incanter core charts]))

;;USAGE
;; (defn rand1 [] (rand 1))
;; (show (time-chart [rand1 rand1]) :title "test random funcs")

(declare component)
(declare grid)


(defn- default-dimensions
  "Returns the default dimensions for a new frame"
  (^java.awt.Dimension []
    (java.awt.Dimension. 500 300)))

(defn- createFrame [title]
  (doto (new JFrame title)
    (.setVisible true)
    (.pack)
    (.setDefaultCloseOperation (. JFrame DISPOSE_ON_CLOSE))))

(defn- label 
  "Creates a JLabel with the given content"
  (^JLabel [s]
    (let [^String s (str s)
          label (JLabel. s JLabel/CENTER)]
      (.setToolTipText label s)
      label)))

(defn- component 
  "Creates a component as appropriate to visualise an object x" 
  (^JComponent [x]
    (cond 
      (instance? JComponent x) x
      ;(instance? BufferedImage x) (JIcon. ^BufferedImage x)
      (instance? JFreeChart x) (ChartPanel. ^JFreeChart x)
      (sequential? x) (grid (seq x))
      :else (label x))))

(defn- grid [things]
  (let [n (count things)
        size (int (Math/ceil (Math/sqrt n)))
        grid-layout (GridLayout. 0 size)
        grid (JPanel.)]
    (.setLayout grid grid-layout)
    (doseq [x things]
      (.add grid (component x)))
    grid))

(defn- display [^JComponent com title]
  (let [f (createFrame title)
        g (.getContentPane f)]
    (do (.add g com)
        (.pack f))
    f))

(defn show 
     "Shows a component in a new frame"
     ([com 
       & {:keys [^String title]
          :as options
          :or {title nil}}]
        (let [com (component com)]
          (display com (str title)))))

(defn xy-chart ^JFreeChart [xs ys]
  (let [chart (incanter.charts/xy-plot xs ys)]
    ; (incanter.charts/set-y-range chart 0.0 1.0)
    chart))

(defn xy-chart-multiline ^JFreeChart [xs yss]
  (let [chart (xy-chart xs (first yss))]
    (doseq [ys (rest yss)] 
      (incanter.charts/add-lines chart xs ys)) 
    chart))

(defn time-chart 
  "Creates a continously updating time chart of one or more calculations, which should be functions with zero arguments."
  [calcs
     & {:keys [repaint-speed time-periods y-min y-max] 
        :or {repaint-speed 250
             time-periods 1200}}]
  (let [line-count (count calcs)
        start-millis (System/currentTimeMillis)
        times (atom '())
        values (atom (repeat line-count '()))
        next-chart  (fn []
                      (let [time (/ (- (System/currentTimeMillis) start-millis) 1000.0)]
                        (swap! times #(take time-periods (cons time %)))
                        (swap! values #(for [[calc ss] (map vector calcs %)]
                                         (take time-periods (cons (calc) ss))))
                        (let [chart (xy-chart-multiline @times @values)]
                          (if y-max (incanter.charts/set-y-range chart (double (or y-min 0.0)) (double y-max)))
                          chart)))
        panel (ChartPanel. ^JFreeChart (next-chart)) 
        timer (javax.swing.Timer. 
               (int repaint-speed) 
               (proxy [java.awt.event.ActionListener] []
                 (actionPerformed 
                   [^ActionEvent e]
                   (when (.isShowing panel) 
                     (.setChart panel ^JFreeChart (next-chart))
                     (.repaint ^JComponent panel)) )))]
    (.start timer)
    (.setPreferredSize panel (default-dimensions))
    panel))

