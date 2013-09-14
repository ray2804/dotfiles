(defproject local-client "0.0.1"
  :description "A local light table project"
  :plugins [[lein-cljsbuild "0.3.2"]
            [ring-mock "0.1.5"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.clojure/math.combinatorics "0.0.4"]
                 [uncomplicate/fluokitten "0.3.0"]
                 ;[org.mozilla/rhino "1.7R3"]
                 ;[com.google.javascript/closure-compiler "v20130823"]
                 ;[org.clojure/google-closure-library "0.0-2029-2"]
                 ;[com.cemerick/pomegranate "0.2.0"]
                 ;[ring/ring-jetty-adapter "1.2.0"]
                 [prismatic/plumbing "0.1.0"]
                 [prismatic/schema "0.1.1"]
                 [garden "1.0.0-SNAPSHOT"]
                 [markdown-clj "0.9.31"]
                 ;[swiss-arrows "0.6.0"]
                 [inflections "0.8.2" :exclude [org.mozilla/rhino
                                                org.clojure/google-closure-library]]
                 [compojure "1.1.5"]
                 [cheshire "5.2.0"]
                 ;[backtick "0.3.0"]
                 [hiccup "1.0.4"]
                 ;[frak "0.1.3"]
                 ]
  :injections [(require '[clojure.zip :as zip]
                        '[clojure.repl :refer [doc source dir]]
                        ;'[clojure.edn :refer [read read-string]]
                        '[clojure.pprint :refer [pprint print-table cl-format]]
                        '[clojure.set :refer [project]]
                        '[clojure.string :as string]
                        '[clojure.test :refer :all]

                        '[uncomplicate.fluokitten core jvm]
                        '[swiss-arrows.core :refer :all]
                        '[backtick :refer [syntax-quote template defquote]]

                        '[compojure.core :refer :all]
                        ;'[ring.adapter.jetty :refer :all]
                        '[compojure.handler :as handler]
                        '[compojure.route :as route]

                        '[hiccup.core :as hiccup :refer [html]]
                        '[hiccup.form :as frm]
                        '[hiccup.page :refer [html5 include-css include-js]]

                        '[garden.core :as garden :refer [css]]
                        '[garden.def :refer [defrule]]
                        '[garden.units :as gunits :refer [px pt em percent]]
                        '[garden.color :as gcolor :refer [hsl]]

                        '[plumbing.graph :as graph]
                        '[plumbing.core :refer :all]

                        '[inflections.core :refer [plural singular underscore
                                                   ordinalize capitalize]]
                        '[markdown.core :as markdown]
                        '[frak :as frak]
                        '[cheshire.core :refer :all]
                        '[schema.core :as schema]

                        ;'[cemerick.pomegranate :refer [add-dependencies]]
                        )])
