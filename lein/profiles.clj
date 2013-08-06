{:user {


        ;; --------------------------------
        ;; Plugins affect leiningen itself
        ;; --------------------------------
        :plugins [[lein-bin "0.3.2"]
                  [lein-open "0.1.0"]
                  [lein-exec "0.3.0"]
                  [lein-swank "1.4.5"]
                  [lein-clique "0.1.0"] ; graphiv function dep graph
                  [lein-light "0.0.16"]
                  [lein-mangoes "0.2.1"]
                  [lein-newnew "0.3.4"] ; Task -> 2.0 codebase, plugin will no longer be developed
                  [lein-depgraph "0.1.0"]
                  [lein-difftest "2.0.0"]
                  [lein-cljsbuild "0.3.2"]
                  [cljs-kickoff/lein-template "0.1.3"]
                  [lein-marginalia "0.7.1"] ;; literate / documentation
                  [lein-tarsier "0.9.4"]    ;; more complete (with project support) vim plugin to clojure/repl
                  [lein-autoreload "0.1.0"] ;; guarantee when running repl you are up-to-date if files change
                  [lein-simpleton "1.1.0"]  ;; localhost http static server in current directory with autoindex
                  [lein-bikeshed "0.1.3"]   ;; notify code smell or bad practice that should make you feel bad
                  ;;[lein-pedantic "0.0.5"] ;; DEPRECATED use `$ lein deps :tree` now
                  [lein-create-template "0.1.1"] ;; create templates from existing projects
                  [lein-pprint "1.1.1"]
                  [lein-annotations "0.1.0"] ;; get TODO, FIXME, HACK etc. comment ;; blocks
                  [lein-clojars "0.9.1"]
                  [lein-localrepo "0.4.1"] ;; local (maven) repositories
                  [lein-sub "0.2.4"]
                  [lein-sitemap "0.1.0"]
                  [lein-kibit "0.0.8"]
                  [lein-ancient "0.3.3"] ;; successor to outdated
                  ;[lein-outdated "1.0.1"] ;; DEPRECATED
                  ;[lein-daemon "0.5.4"] ;; FIXME
                  ;[lein-scrooge "0.1.1"] ;; FIXME
                  [lein-checkouts "1.1.0"]
                  [lein-deps-tree "0.1.2"]
                  [lein-ritz "0.7.0"]
                  [lein-idefiles "0.2.0"]
                  [lein-environ "0.4.0"]
                  [lein-ring "0.8.5"]
                  [lein-immutant "0.17.1"]
                  [codox "0.6.4"]           ;; (lein doc) automatic API documentation including link to source code
                  ]




        ;; -----------------------------------
        ;; These are useful within any project
        ;; -----------------------------------
        :dependencies

        [[org.clojure/clojure "1.5.1"]

         #_ "Core libraries
         ------------------
         NOTE: These API's, although found in core may, not be stable. Use wisely."
         [org.clojure/core.logic "0.8.3"]
         [org.clojure/core.unify "0.5.6"]
         [org.clojure/core.typed "0.1.22"]
         [org.clojure/core.cache "0.6.3"]

         #_ "Data processing libraries"
         ;[org.clojure/data.finger-tree "0.0.1"]
         [org.clojure/data.csv "0.1.2"]
         [org.clojure/data.zip "0.1.1"]
         [org.clojure/data.codec "0.1.0"]
         [org.clojure/data.json "0.2.2"]

         #_ "Tools"
         [org.clojure/clojure-contrib "1.2.0"]

         [org.clojure/algo.monads "0.1.4"]

         [stencil "0.3.2"] ; A fast, compliant implementation of Mustache in Clojure.

         ;[storm "0.9.0-wip21"]

         [org.clojure/jvm.tools.analyzer "0.4.2"]
         [org.clojure/tools.namespace "0.2.4"]
         ;[org.clojure/tools.logging "0.2.6"]
         [org.clojure/tools.reader "0.7.5"]
         [org.clojure/tools.trace "0.7.5"]

         [backtick "0.3.0"] ; macros quasiquoting

         [clj-det-enc "1.0.0"] ; A detect encoding utility. juniversalchardet wrapper
         [clj-glob "1.0.0"] ; Finds files based on glob patterns like "*.jpg" or "/usr/*/se*".

         #_ "Math"
         [org.clojure/math.combinatorics "0.0.4"]

         #_ "Servers"
         [http-kit "2.1.8"]
         ;[com.novemberain/langohr "1.2.0"]
         [org.webbitserver/webbit "0.4.15"]

         #_ "Routing"
         [compojure "1.1.5"]
         [ring/ring-json "0.2.0"]

         #_ "Data structures"
         [net.cgrand/megaref "0.3.0"]
         ;[com.stuartsierra/clojure.walk2 "0.1.0-SNAPSHOT"]

         #_ "File system"
         [me.raynes/fs "1.4.5"]
         [image-resizer "0.1.5"] ; no native dependencies
         [clojurewerkz/urly "1.0.0"]

         #_ "Pluggable"
         [com.palletops/chiba "0.2.0"]
         [com.cemerick/pomegranate "0.2.0"] ; dynamic runtime modification of the classpath/pull-in deps

         #_ "(Context free) grammars and parsers"
         [instaparse "1.2.2"]

         #_ "Domain Specific Languages"
         ;; * html          * html/css/dom   * css
         [hiccup "1.0.4"]
         [enlive "1.1.1"]
         [garden "0.1.0-beta5"]
         [hdom "1.0.2"] ; manipulate hiccup data
         [markdown-clj "0.9.29"]
         [dom-css "1.0.3"]
         ;[org.lpetit/net.cgrand.regex "0.0.1"] ; composable regex DSL

         [com.github.kyleburton/clj-xpath "1.4.1"] ; xhtml paths

         ;[alembic "0.1.3"]

         #_ "Data storage solutions"
         ;[com.datomic/datomic "0.8.3335" :exclusions [org.slf4j/slf4j-nop]]

         ;[org.quartz-scheduler/quartz "2.2.0"]
         ;[clojurewerkz/quartzite "1.1.0"]

         #_ "Templating solutions"
         ;[de.ubercode.clostache/clostache "1.3.1"] ; {{handlebars}}
         ;[net.cgrand/moustache "1.2.0-alpha2"]

         ;[cssgen "0.3.0-alpha1"]

         #_ "Console and virtual terminal"
         ;[jline "2.11"] ; term input

         #_ "Search and find"
         ;[query-extractor "0.0.9"] ; search engine q=

         #_ "Multithreaded apps"
         ;[me.hspy/defrecord2 "1.0.0-SNAPSHOT"]
         [defrecord2 "1.0.1"]

         #_ "Graphs, nodes, edges, vertices"
         [criterium "0.4.1"] ; dgraph for fn

         #_ "Monadic functors"
         [swiss-arrows "0.6.0"] ; -<> -<< etc.

         #_ "Fault tolerance"
         ;[clj-stacktrace "0.2.6"]
         [dire "0.4.3"] ; supervise/pre/post-conditions/handlers
         ;[trammel "0.7.0"] ; contracts
         [org.blancas/morph "0.3.0"]

         #_ "Sessions, DOM, OAuth etc"
         [lib-noir "0.6.6"]
         [prismatic/dommy "0.1.1"]

         #_ "Refactoring"
         [slamhound "1.3.3"] ; ns reconstructuring

         #_ "Natural Language Processing and AI"
         [clojure-opennlp "0.3.1"]
         [inflections "0.8.1"] ; plurals

         #_ "Date, time, timers, schedulers, pools, sync"
         [clj-time "0.5.1"] ; joda-time wrappers
         [overtone/at-at "1.2.0"] ; future fn calls timer

         #_ "Hash, encoding, encryption, obfuscation, CRC"
         ;[digest "1.4.3"] ; providing md5, sha-256

         #_ "Parsers (permissive)"
         ;[clj-tagsoup "0.3.0" :exclusions [net.java.dev.stax-utils/stax-utils]]
         ;[clj-tagsoup "0.3.0"]

         [ring/ring-core "1.2.0"]
         [ring/ring-jetty-adapter "1.2.0"]

         ; todo classify
         ;[formative "0.7.2"]
         [spyscope "0.1.3" :exclusions [clj-time]]
         [javert "0.1.0"]
         ;[org.clojars.mikejs/ring-gzip-middleware "0.1.0-SNAPSHOT"]
         [environ "0.4.0"]

         #_ "Disabled"
         ;[clojail "1.0.6"] ; e.g. for clojurebot
         ;[itsy "0.1.1"] ; webspider/crawler
         ;[clj-http "0.7.3"] ; http client
         ;[me.raynes/conch "0.5.1"]
         ;[info.sunng/ring-jetty9-adapter "0.1.0"] ; newest jetty
         ;[clj-webdriver "0.6.0"]
         ;[org.clojars.gnarmis/sentimental "0.1.1-SNAPSHOT"] ; sentiment detection

         ]

        :aliases {"slamhound" ["run" "-m" "slam.hound"]}}

;;  :injections [(require '(clojure.pprint :refer [pprint print-table]))
;;               ;(require '(clojure.tools.namespace repl find))
;;               ;(require '(dire.core :refer [with-handler!]))
;;               ; try/catch to workaround an issue where `lein repl` outside a project dir
;;               ; will not load reader literal definitions correctly:
;;               (try
;;                 (require 'spyscope.core)
;;                 (catch RuntimeException e))
;;               (let [orig (ns-resolve (doto 'clojure.stacktrace require) 'print-cause-trace)
;;                     new (ns-resolve (doto 'clj-stacktrace.repl require) 'pst)]
;;                 (alter-var-root orig (constantly @new)))]

 :repl-options
 {:prompt (fn [ns] (str "your command for <" ns ">, master? " ))
  ;; What to print when the repl session starts.
  :welcome (println "Welcome to the magical world of the repl!")}}

