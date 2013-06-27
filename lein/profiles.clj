{:user {:dependencies
        [[clj-stacktrace "0.2.6"]
         ;[enlive "1.1.1"]
         [org.clojure/clojure-contrib "1.2.0"]
         ;[org.clojure/tools.namespace "0.2.3"]
         [org.clojure/tools.trace "0.7.5"]
         ;;#_easier_multi_threaded_apps
         [spyscope "0.1.3" :exclusions [clj-time]]
         ;[clojail "1.0.6"]
         [javert "0.1.0"]
         [criterium "0.4.1"]
         ;[clj-http "0.7.3"]
         [garden "0.1.0-beta5"]
         ;[me.raynes/conch "0.5.1"]
         ;[info.sunng/ring-jetty9-adapter "0.1.0"]
         ;[clj-webdriver "0.6.0"]
         [slamhound "1.3.3"]]

        ;; --------------------------------
        ;; Plugins affect leiningen itself
        ;; --------------------------------
        :plugins [[lein-light "0.0.16"]
                  [lein-open "0.1.0"]
                  [lein-difftest "2.0.0"]
                  [lein-marginalia "0.7.1"]
                  [lein-exec "0.3.0"]
                  [lein-bin "0.3.2"]
                  [lein-swank "1.4.5"]
                  [lein-tarsier "0.9.4"]    ;; more complete (with project support) vim plugin to clojure/repl
                  [lein-autoreload "0.1.0"] ;; guarantee when running repl you are up-to-date if files change
                  [lein-simpleton "1.1.0"]  ;; localhost http static server in current directory with autoindex
                  [lein-bikeshed "0.1.3"]   ;; notify code smell or bad practice that should make you feel bad
                  ;;[lein-pedantic "0.0.5"] ;; DEPRECATED use `$ lein deps :tree` now
                  [lein-pprint "1.1.1"]
                  [lein-annotations "0.1.0"] ;; get TODO, FIXME, HACK etc. comment ;; blocks
                  [lein-clojars "0.9.1"]
                  [lein-localrepo "0.4.1"]
                  [lein-sub "0.2.4"]
                  [lein-kibit "0.0.8"]
                  [lein-ancient "0.3.3"] ;; successor to outdated
                  ;[lein-outdated "1.0.1"] ;; DEPRECATED
                  [lein-sitemap "0.1.0"]
                  ;[lein-daemon "0.5.4"] ;; FIXME
                  [lein-clique "0.1.0"]
                  ;[lein-scrooge "0.1.1"] ;; FIXME
                  [lein-create-template "0.1.1"]
                  [lein-checkouts "1.1.0"]
                  [lein-deps-tree "0.1.2"]
                  [lein-ritz "0.7.0"]
                  [lein-idefiles "0.2.0"]
                  [lein-environ "0.4.0"]
                  [lein-ring "0.8.5"]
                  [lein-cljsbuild "0.3.2"]
                  [lein-immutant "0.17.1"]
                  [codox "0.6.4"]           ;; (lein doc) automatic API documentation including link to source code
                  ]

        :aliases {"slamhound" ["run" "-m" "slam.hound"]}}

 :injections [(require '(clojure.tools.namespace repl find))
              ; try/catch to workaround an issue where `lein repl` outside a project dir
              ; will not load reader literal definitions correctly:
              (try
                (require 'spyscope.core)
                (catch RuntimeException e))
              (let [orig (ns-resolve (doto 'clojure.stacktrace require) 'print-cause-trace)
                    new (ns-resolve (doto 'clj-stacktrace.repl require) 'pst)]
                (alter-var-root orig (constantly @new)))]

 :repl-options
 {:prompt (fn [ns] (str "your command for <" ns ">, master? " ))
  ;; What to print when the repl session starts.
  :welcome (println "Welcome to the magical world of the repl!")}}

