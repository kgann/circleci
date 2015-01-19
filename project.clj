(defproject kgann/circleci "0.2.0-SNAPSHOT"
  :description "Interface to the CircleCI REST API"
  :url "https://github.com/kgann/circleci"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.macro "0.1.2"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]

                 [cheshire "5.4.0"]
                 [http-kit "2.1.18"]]

  :profiles {:dev {:dependencies [[http-kit.fake "0.2.2"]]
                   :plugins [[codox "0.8.10"]]
                   :codox {:src-dir-uri "http://github.com/kgann/circleci/tree/master/"
                           :src-linenum-anchor-prefix "L"
                           :defaults {:doc/format :markdown}}}})
