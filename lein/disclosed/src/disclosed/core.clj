(ns disclosed.core
  ^{:doc "Quick reference guide for common concepts to study,
    learn and remember by repetition and mnemonical syntaxis"
    :author "Rob Jentzema"}
  
  ;; I'm lazy, I am lazy, you're lazy - you are lazy
  (:require [clojure.repl :as repl  :refer [doc dir source]]
            [clojure.string :as string  :refer [split join]]
            [clojure.set :as set  :refer [difference index]]
            [clojure.pprint :as pprint :refer [print-table]]
            [clojure.reflect  :as reflect  :refer [reflect]]
            [clojure.data.json :as json]
            [clj-http.client :as client]            
            ))


;(json/read-str (str (client/get "http://registry.npmjs.org/coffee-script")))

;(dir clojure.data.json)
;(json/pprint "asdd")
;(dir clojure.)
;(client/get "http://google.com")


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(foo "bah")

(defn create-board 
  "Returns a hash-map of :game :home :bar and :dice vectors.
  Shall be assigned a Var and passed assed argument of consequetive
  functions to create the impression of mutable state."
  [] {:game (vec (repeat 24 [])) :home [] :bar [] :dice []})

(defn plot-maps
  "Layout game board"
  []
  (println (repeat 24 (apply vector '(|.|)))))

(plot-maps)
(create-board)