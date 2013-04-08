;;; ~/.lein/profiles.clj

;; You should really <url:#readme> *(in vim use :Utl on this link)
;; The :plugins [[vector]] of my :user profile.clj file

{:user {:plugins [
                  ;; Contrib
                  [org.clojure/data.json "0.2.2"]     ; Clojure JSON library
                  ;[lein-daemon "0.5.1"]              ; FIXME Lein plugin that starts a clojure process as a daemon.
                  ;[clucy "0.3.0"]                    ; Flagship sub-project, provides Java-based indexing and search technology.

                  ;; Basic tools
                  ;;
                  ;[lein-search "0.3.4"]              ; FIXME Allows searching and adding of dependencies on clojars
                  [lein-libdir "0.1.1"]               ; Copy jar dependencies into the project lib directory.

                  ;; Visualize
                  [lein-depgraph "0.1.0"]             ; Iterate the project namespace tree and generate Graphviz .svg

                  ;; Shell, vterm and console tools
                  [clojure-lanterna "0.9.3"]          ; Curses but then in Clojure :)
                  [lein-shell "0.1.0"]                ; Leiningen plugin for calling shell commands.
                  [lein-exec "0.3.0"]                 ; <url:#examples-leinexec> Executable ClojureScript
                                                      ; scripts in the shell, project-scope (exec-p)
                                                      ; or outside, in the system global using lein-exec.

                  ;; Literate programming and documentation

                  [lein-marginalia "0.7.1"]           ; Ultra-lightweight literate programming for Clojure and ClojureScript.

                  ;; Servers and REPL

                  [lein-swank "1.4.0"]                ; Launch a swank server to connect emacs to.

                  [lein-tarsier "0.10.0"]             ; Fully-featured Leiningen plugin to run a VimClojure server for Lein 1 & 2 projects.
                  ;[appengine-magic "0.5.0"]           ; FIXME Abstract away the infra nuts and bolts of writing a Clojure application for GAE

                  ;; Mobile platforms

                  [lein-droid "0.0.6"]                ; Plugin for easy Clojure/Android development and deployment


                  ;; Clients

                  [clj-http "0.6.5"]                  ; An idiomatic clojure http client wrapping the apache client.
                  [org.clojars.kriyative/clj-http-client "1.0.0"]

                  ;; IDE Integrated Development Environments

                  [lein-catnip "0.5.1"]               ; A web based and Clojure IDE providing a fully functional text editor and REPL environment for CLJS.
                  [lein-light "0.0.24"]               ; LightTable Playground plugin to connect to the IDE
                  [org.clojars.scott/lein-nailgun "1.1.0"] ; A leiningen plugin to launch a vimclojure nailgun server

                  ;; Web Domain Specific

                  [lein-cssgenbuild "0.2.1"]          ; Generate stylesheets from cssgen s-expressions.
                  [hiccup-bootstrap "0.1.1"]          ; Generate stylesheets from cssgen s-expressions.


                  ;; References

                  [clojurewerkz/urly "1.0.0"]         ; <url:#urly>Unifies parsing of URIs, URLs & the likes such as relative href values in real-world HTML.

                  ;; TDD, code coverage, integration

                  [lein-cloverage "1.0.2"]            ; A Leiningen plugin to run cloverage test coverage.
                  [lein-midje "3.0.0"]                ; Leiningen plugin for Test Driven Development (TDD) with Midje.
                  ;[lein-guzheng "0.4.5"]             ; FIXME (hangs) Performs branch coverage analysis on your project

                  ;; Creative entertainment

                  [overtone "0.8.1"]                  ; Programmable collaborative music studio online


                  ;[bigml/histogram "3.1.0"]          ; Streaming Parallel Decision Trees implemented as one-pass histograms parallel boosted.
                  [jackknife "0.1.2"]                 ; Clojure tools used in Cascalog, ElephantDB, Storm and others.
                  ;[me.raynes/conch "0.5.1"]          ; Two libraries: simple low-level interface to Java process APIs & 1 inspired by Python sh.
                  ;[codox "0.6.4"]                    ; A tool for generating API documentation from Clojure source code.
                  ;[lein-httpd "1.0.0"]               ; Start a web server in the current directory.
                  ;[org.timmc/lein-otf "2.0.0"]       ; Produce non-AOT uberjars OTF (on-the-fly)
                  [lein-aggravate "0.1.0-SNAPSHOT"]   ; Concatenate and compress files like .css or .js and .coffee
                  ;[lein-bin "0.3.2"]
                  ;[lein-difftest "2.0.0"]
                  ;[lein-embongo "0.2.0"] ; run 'embedded' instance of MongoDB during build (e.g. for integration testing)
                  [lein-pprint "1.1.1"]

                  [cljs-template "0.1.5"]
                  [lein-cljsbuild "0.1.8"]
                  ;[joodo/lein-joodo "1.1.1"]
                  [lein-gitify "0.1.0"]               ; <url:#gitify>Plugin for creating and initializing a Github repository.
                  ;[perforate "0.3.0"]                ;
                  ;[alandipert/desiderata "1.0.1"]
                  ;[org.clojure/math.combinatorics "0.0.3"]
                  ;[lein-webapp-template/lein-template "1.3.0"]
                  ;[org.clojure/tools.cli "0.2.2"]
                  ;[org.clojars.trptcolin/lein-search "0.3.3"]
                  ;[clj-stacktrace "0.2.4-SNAPSHOT"]
                  [lein-ring "0.6.4"]
                  ;[antler/lein-caribou "1.4.8"]
                  ;[lein-swank "1.4.4"]
                  ]

  :search-page-size 30
  :repl-options {:prompt (fn [ns] (str "My liege? "))
                 }}}


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;id=readme
;; The `:user` profile is separate from `:dev`; the latter is intended to be
;; specified in the project itself. In order to avoid collisions, the project
;; should never define a `:user` profile, nor should `~/.lein/profiles.clj`
;; define a `:dev` profile. Use the `show-profiles` task to see what's
;; available.
;;


;; id=urly

;; id=gitify
;; This plugin requires that you have set your Github username and password
;; in your git config, usually located at ~/.gitconfig
;; git config --get github.user
;; git config --get github.password


;--------------------------------------------------------------------------------
;id=examples-leinexec
;--------------------------------------------------------------------------------
;; Lein-exec plugin can pull in dependencies just like 'snap' from Maven, Central etc.
;(use '[leiningen.exec :only (deps)])
;(deps '[[ring/ring-core "1.0.0"]
;        [ring/ring-jetty-adapter "1.0.0"]])
;(deps '[[foo/bar "1.2.3"]]
;      :repositories {"myrepo" "http://mycorp.com/repositories/"})
;========================================================================================
;
;
;;; List
;;;
;;; [net.mikera/vectorz-clj "0.4.1"]


