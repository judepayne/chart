(defproject chart "0.1.0-SNAPSHOT"
  :description "Real time chart from zero arity clojure functions"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [incanter "1.5.5"]
                 [org.jfree/jfreechart "1.0.19"]]
  :signing {:gpg-key "5C92FAF1"}
  :deploy-repositories [["clojars" {:creds :gpg}]])
