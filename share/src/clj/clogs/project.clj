(defproject clogs "0.1.0-SNAPSHOT"

  :description "Clojure GS (Generator Scripts)"

  :url "http://clj-no.de/clogs"

  :license {:name "Eclipse Public License"
  
  :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1586"]    ; transcompile to js
                 [hiccup "1.0.3"]              ; DSL to HTML
                 [hiccups "0.2.0"]             ; HTML to ClojureScript
                 [hiccup-bootstrap "0.1.1"]    ; ITwitterBootstrap
                 [hiccup-pipeline "0.1.0"]     ; HTML - DSL - HTML
                 [compojure "1.1.5"]           ; routing framework
                 
                 ; append extra deps
                 ])


