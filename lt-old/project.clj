(defproject local-client "0.0.1"

  :description "My hacked local light table project"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.cemerick/pomegranate "0.2.0"]
                 [swiss-arrows "0.6.0"]
                 [backtick "0.3.0"]
                 [prismatic/plumbing "0.1.0"]
                 ]

  :injections [(require '[clojure.repl :refer [doc source dir]]
                        '[clojure.pprint :refer [pprint print-table]]
                        '[clojure.string :as string]
                        '[swiss-arrows.core :refer :all]
                        '[backtick :refer :all]
                        '[plumbing.core :refer :all]
                        '[cemerick.pomegranate :refer [add-dependencies]]
                        )]
  )

