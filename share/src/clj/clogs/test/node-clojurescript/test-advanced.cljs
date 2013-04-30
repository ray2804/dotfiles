(ns hello
  (:require [cljs.nodejs :as nodejs]            
            [clojure.string :as string]))

(def http
  (nodejs/require "http"))

(def server (.createServer http
   (fn [req, res]
     (.writeHead res 200 (clj->js {:Content-Type "text/plain"}))
     (.end res "Hello, World\n"))))

(.listen server 4200 "127.0.0.1")

(println "Server running at http://127.0.0.1:4200")

;; Helper
(defn clj->js
  "Recursively transforms ClojureScript maps into Javascript objects,
   other ClojureScript colls into JavaScript arrays, and ClojureScript
   keywords into JavaScript strings. Credit:
   http://mmcgrana.github.com/2011/09/clojurescript-nodejs.html"
  [x]
  (cond
    (string? x) x
    (keyword? x) (name x)
    (map? x) (.-strobj (reduce (fn [m [k v]]
               (assoc m (clj->js k) (clj->js v))) {} x))
    (coll? x) (apply array (map clj->js x))
    :else x))
