(defproject
  tredius
  "0.1.0-SNAPSHOT"
  :dependencies
  [[org.clojure/clojure "1.5.1"]
   [org.clojure/clojure-contrib "1.2.0"]
   [lib-noir "0.6.0"]
   [compojure "1.1.5"]
   [ring-server "0.2.8"]
   [clabango "0.5"]
   [com.taoensso/timbre "1.6.0"]
   [com.postspectacular/rotor "0.1.0"]
   [com.taoensso/tower "1.5.1"]
   [markdown-clj "0.9.25"]
   [org.clojure/clojurescript "0.0-1806"]
   [domina "1.0.0"]
   [prismatic/dommy "0.1.1"]
   [cljs-ajax "0.1.1"]
   [http-kit "2.1.3"]
   [org.clojure/java.jdbc "0.2.3"]
   [com.h2database/h2 "1.3.170"]
   [korma "0.3.0-RC5"]
   [log4j
    "1.2.15"
    :exclusions
    [javax.mail/mail
     javax.jms/jms
     com.sun.jdmk/jmxtools
     com.sun.jmx/jmxri]]]
  :cljsbuild
  {:builds
   [{:source-paths ["src-cljs"],
     :compiler
     {:pretty-print false,
      :output-to "resources/public/js/site.js",
      :optimizations :advanced}}]}
  :ring
  {:handler tredius.handler/war-handler,
   :init tredius.handler/init,
   :destroy tredius.handler/destroy}
  :profiles
  {:production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}},
   :dev
   {:dependencies [[ring-mock "0.1.3"] [ring/ring-devel "1.1.8"]]}}
  :url
  "http://tredius.nl"
  :main
  tredius.core
  :plugins
  [[lein-ring "0.8.5"] [lein-cljsbuild "0.3.2"]]
  :description
  "FIXME: write description"
  :min-lein-version "2.0.0")