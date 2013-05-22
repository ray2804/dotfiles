#_(defdeps
    [[org.clojure/clojure "1.5.1"]
     [compojure "0.5.2"]
     [ring/ring-jetty-adapter "0.3.3"]])

(ns example
  (:use [compojure.core :only [defroutes GET]]
        [ring.adapter.jetty :only [run-jetty]]))

(defroutes routes
  (GET "/" [] "Hello world!"))

(def server
  (run-jetty routes {:port 8080 :join? false}))