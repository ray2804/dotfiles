(ns disclosed.core
  ^{:doc "Quick reference guide for common concepts to study"
    :author "Rob Jentzema"}
  
  ;; I'm lazy
  (:require [clojure.repl :as repl  :refer [doc dir source]]
            [clojure.string :as string  :refer [split join]]
            [clojure.set :as set  :refer [difference index]]
            [clojure.pprint :as pprint :refer [print-table]]
            [clojure.reflect  :as reflect  :refer [reflect]]
            [clojure.data.json :as json]
            ))

;(dir clojure.data.json)
(json/pprint "asdd")
;(dir clojure.)


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
