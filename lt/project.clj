(defproject local-client "0.0.1"
  :description "A local light table project"
  :plugins [[lein-cljsbuild "0.3.2"]
            [ring-mock "0.1.5"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.clojure/math.combinatorics "0.0.4"]
                 [org.clojure/data.generators "0.1.2"]
                 [uncomplicate/fluokitten "0.3.0"]
                 [org.clojure/data.json "0.2.3"]
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
                 [overtone "0.9.0-SNAPSHOT"]
;;                  [inflections "0.8.2" :exclude [org.clojure/data.json
;;                                                 org.mozilla/rhino
;;                                                 org.clojure/google-closure-library]]

                 [compojure "1.1.5"]
                 [nstools "0.2.5"]
                 [http-kit "2.1.11"]
                 [cheshire "5.2.0"]
                 [backtick "0.3.0"]
                 [hiccup "1.0.4"]
                 [frak "0.1.3"]
                 ]
  :injections [
               ;(refer-clojure :exclude [read read-string alter commute ref-set ensure])
               (require '[clojure.zip :as zip]
                        '[clojure.java.shell :refer [sh]]
                        '[clojure.repl :refer [doc source dir]]
                        '[clojure.walk :as walk]
                        '[clojure.edn :as edn]
                        '[clojure.pprint :as pprint]
                        '[clojure.math.combinatorics :as combo]
                        '[clojure.set :as set]
                        '[clojure.string :as string]
                        '[clojure.test :as tst]
                        '[clojure.contrib.math :as math]
                        '[clojure.data.generators :as datagen]

                        '[uncomplicate.fluokitten core jvm]
                        '[swiss-arrows.core :refer :all]
                        '[backtick :refer [syntax-quote template defquote]]

                        '[compojure.core :refer :all]
                        ;'[ring.adapter.jetty :refer :all]
                        '[compojure.handler :as handler]
                        '[compojure.route :as route]

                        '[hiccup.core :as hiccup :refer [html]]
                        '[hiccup.form :as hicfrm]
                        '[hiccup.page :as page]

                        '[garden.core :as garden]
                        '[garden.def :as gardef]
                        '[garden.units :as gunits :refer [px pt em percent]]
                        '[garden.color :as gcolor :refer [hsl]]

                        '[net.cgrand.megaref :as mega]

                        '[plumbing.core :as pp]
                        '[plumbing.fnk.schema :as fnkschema]
                        '[plumbing.fnk.pfnk :as pfnk]
                        '[plumbing.graph :as graph]
                        '[plumbing.map :as plumap]
                        '[nstools.ns :as nstools]

                        ;; Linguistics
                        ;'[inflections.core :refer [plural singular underscore
                        ;                           ordinalize capitalize]]

                        '[markdown.core :as markdown]
                        '[frak :as frak]
                        '[cheshire.core :as cjson]
                        '[schema.core :as schema]
                        '[org.httpkit.server :as httpkit]
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

               ;;;
               ;;; Numerical ranges
               ;;;
               (defn n-repeat [n] (lazy-cat (repeat n n) (n-repeat (inc n))))

               ;;;
               ;;; String writing
               ;;;
               (defmacro with-out-str-and-value
                 [& body]
                 `(let [s# (new java.io.StringWriter)]
                    (binding [*out* s#]
                      (let [v# ~@body]
                        (vector (str s#)
                                v#)))))

               ;;;
               ;;; Lookup
               ;;;

               (def fyrst (comp symbol str first str))
               (defmacro dir-split [ns-symbol]
                 `(clojure.string/split-lines (with-out-str (clojure.repl/dir ~ns-symbol))))
               ;(def dir-lookup (group-by fyrst (dir-split overtone.core)))


               ])
