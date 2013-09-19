(defproject local-client "0.0.1"
  :description "A local light table project"
  :plugins [[lein-cljsbuild "0.3.2"]
            [ring-mock "0.1.5"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.clojure/math.combinatorics "0.0.4"]
                 [org.clojure/data.generators "0.1.2"]
                 [uncomplicate/fluokitten "0.3.0"]
                 ;[org.mozilla/rhino "1.7R3"]
                 ;[com.google.javascript/closure-compiler "v20130823"]
                 ;[org.clojure/google-closure-library "0.0-2029-2"]
                 ;[com.cemerick/pomegranate "0.2.0"]
                 ;[ring/ring-jetty-adapter "1.2.0"]
                 [prismatic/plumbing "0.1.0"]
                 [net.cgrand/megaref "0.3.0"]
                 [prismatic/schema "0.1.3"]
                 [garden "1.0.0-SNAPSHOT"]
                 [markdown-clj "0.9.32"]
                 [swiss-arrows "0.6.0"]
                 [cupboard "1.0beta1"]
                 [inflections "0.8.2" :exclude [org.mozilla/rhino
                                                org.clojure/google-closure-library]]
                 [compojure "1.1.5"]
                 [http-kit "2.1.11"]
                 [cheshire "5.2.0"]
                 [backtick "0.3.0"]
                 [hiccup "1.0.4"]
                 [frak "0.1.3"]
                 ]
  :injections [(refer-clojure :exclude [read read-string alter commute ref-set ensure])
               (require '[clojure.zip :as zip]
                        '[clojure.java.shell :refer [sh]]
                        '[clojure.repl :refer [doc source dir]]
                        '[clojure.walk :as walk]
                        '[clojure.edn :refer [read read-string]]
                        '[clojure.pprint :refer [pprint print-table cl-format]]
                        '[clojure.math.combinatorics :as combo
                          :refer [combinations subsets cartesian-product selections permutations]]
                        '[clojure.set :refer [difference index intersection join map-invert project
                                              rename rename-keys select subset? superset? union]]
                        '[clojure.string :as string]
                        '[clojure.test :refer :all]
                        '[clojure.contrib.math :refer :all]
                        '[clojure.data.generators :as datagen]

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

                        '[net.cgrand.megaref :as mega
                          :refer [alter commute ref-set ensure subref]]

                        '[plumbing.core :refer :all]
                        '[plumbing.fnk.schema :as fnkschema]
                        '[plumbing.fnk.pfnk :as pfnk]
                        '[plumbing.graph :as graph]
                        '[plumbing.map :as map]

                        ;; Linguistics
                        '[inflections.core :refer [plural singular underscore
                                                   ordinalize capitalize]]

                        '[markdown.core :as markdown]
                        '[frak :as frak]
                        '[cheshire.core :refer :all]
                        '[schema.core :as schema]
                        '[org.httpkit.server :refer :all]

                        ;'[cemerick.pomegranate :refer [add-dependencies]]
                        )
               (import '(java.util.concurrent Executors))

               ;;;
               ;;; Concurrency
               ;;;

               (def *pool* ^:dynamic (Executors/newFixedThreadPool
                            (+ 2 (.availableProcessors (Runtime/getRuntime)))))

               (defn dothreads [f & {thread-count :threads exec-count :times :or {thread-count 1 exec-count 1}}]
                 (dotimes [t thread-count]
                   (.submit *pool* #(dotimes [_ exec-count] (f)))))

               ;; Atoms are thread safe:
               ;;  (def ticks (atom 0))
               ;;  (defn tick [] (swap! ticks inc))
               ;;  (dothreads tick :threads 1000 :times 100)
               ;;  @ticks ;=> 100000

               ;;;
               ;;; Lambda
               ;;;

               (defmacro def-curry-fn [name args & body]
                 {:pre [(not-any? #{'&} args)]}
                 (if (empty? args)
                   `(defn ~name ~args ~@body)
                   (let [rec-funcs (reduce (fn [l v]
                                             `(letfn [(helper#
                                                       ([] helper#)
                                                       ([x#] (let [~v x#] ~l))
                                                       ([x# & rest#] (let [~v x#]
                                                                       (apply (helper# x#) rest#))))]
                                                helper#))
                                           `(do ~@body) (reverse args))]
                     `(defn ~name [& args#]
                        (let [helper# ~rec-funcs]
                          (apply helper# args#))))))

               ;;;
               ;;; Indexes and filters
               ;;;

               (defn has-indexed [coll] (map vector (iterate inc 0) coll))

               (defn index-filter
                 "From Stu Halloway's article at
                 http://thinkrelevance.com/blog/2009/08/12/rifle-oriented-programming-with-clojure-2"
                 [pred coll]
                 (when pred
                   (for [[idx elt] (has-indexed coll) :when (pred elt)] idx)))


               ])
