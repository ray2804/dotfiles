;;; ~/.lein/profiles.clj
;;; Leiningen global base :user profile for projects (common libraries)

;; Leiningen profiles
;; ==================

;; Local project and profiles files (:dev)
;; --------------------------------

;; In addition to the local `project.clj`, profiles also can be specified in 
;; `profiles.clj` within the project root. Profiles in =project= `profiles.clj` 
;; will override those in `project.clj`.

;; The `:dev` profile should go here. Never define a `:user` profile here!

;; Global profiles (~/.lein/..) (:users)
;; ----------------------------

;; Global profiles will be available in all projects managed by Leiningen, though
;; those profiles will be **overridden** by profiles of the same name specified in 
;; the project. Defining the same global profile in multiple different files is 
;; considered an error.

;; The `:user` profile is separate from `:dev`; the latter is intended to be
;; specified in the project itself. In order to avoid collisions, the project
;; should never define a `:user` profile, nor should a global `:dev` profile be
;; defined. Use the `show-profiles` task to see what's available.

{:user 
 {:plugins
  [
   ;; Contrib
   [org.clojure/data.json "0.2.2"]     ; Clojure JSON library
   ;[lein-daemon "0.5.1"]              ; FIXME Lein plugin that starts a clojure process as a daemon.
   ;[clucy "0.3.0"]                    ; Flagship sub-project, provides Java-based indexing and search technology.
   
   ;; Tools
   ;;
   ;[lein-search "0.3.4"]              ; FIXME Allows searching and adding of dependencies on clojars
   ;[org.clojars.trptcolin/lein-search "0.3.3"]
   [alandipert/desiderata "1.0.1"]    ; A ClojureScript bag of tricks. 
  
   ;; Source control
   ;;
   [lein-gitify "0.1.0"]               ; <url:#gitify>Plugin for creating and initializing a Github repository.
   
   ;; Math, statistics and probability
   ;;
   ;[org.clojure/math.combinatorics "0.0.3"]
   
   ;; Project management and bootstrapping
   ;;
   [lein-cljsbuild "0.1.8"]            ; Leiningen plugin to make ClojureScript development easy.
   ;[org.timmc/lein-otf "2.0.0"]       ; Produce non-AOT uberjars OTF (on-the-fly)
   [lein-libdir "0.1.1"]               ; Copy jar dependencies into the project lib directory.
   [lein-pprint "1.1.1"]               ; Pretty-print a representation of the project map.
   
   ;; Templates
   ;;
   [cljs-template "0.1.5"]             ; *DEPRECATED* A Leiningen template for Noir + ClojureScript projects.
   ;[lein-webapp-template/lein-template "1.3.0"] ; Web apps based on Compojure, Stencil, Bootstrap and jQuery
   
   
   ;; Collections, data, groups and sets
   ;;
   [edw/ordered "1.3.2"]               ; Less bloated pure Clojure implementation of ordered hash-sets
   ;[lein-embongo "0.2.0"]             ; run 'embedded' instance of MongoDB during build (e.g. for integration testing)
   [jackknife "0.1.2"]                 ; Clojure tools used in Cascalog, ElephantDB, Storm and others.
   
   ;; Visualize
   ;;
   [lein-depgraph "0.1.0"]             ; Iterate the project namespace tree and generate Graphviz .svg
   ;[bigml/histogram "3.1.0"]          ; Streaming Parallel Decision Trees implemented as one-pass histograms parallel boosted.
   
   ;; Shell, vterm and console tools
   ;;
   [clojure-lanterna "0.9.3"]          ; Curses but then in Clojure :)
   [lein-shell "0.1.0"]                ; Leiningen plugin for calling shell commands.
   [lein-exec "0.3.0"]                 ; <url:#ex-leinexec> Executable ClojureScript in the shell and project-scope shell.
   [lein-bin "0.3.2"]                  ; Standalone console executables for your project.
   ;[me.raynes/conch "0.5.1"]          ; Two libraries: simple low-level interface to Java process APIs & 1 inspired by Python sh.
   [org.clojure/tools.cli "0.2.2"]     ; The tools.cli package is a command line argument parser for Clojure.
   

   ;; Literate programming and documentation
   ;;
   [lein-marginalia "0.7.1"]           ; Ultra-lightweight literate programming for Clojure and ClojureScript.
   [codox "0.6.4"]                     ; A tool for generating API documentation from Clojure source code.
   
   ;; Text utilities
   ;;
   [lein-aggravate "0.1.0-SNAPSHOT"]   ; Concatenate and compress files like .css or .js and .coffee
   
   ;; Servers and REPL
   ;;
   [lein-swank "1.4.0"]                ; Launch a swank server to connect emacs to.
   [lein-tarsier "0.10.0"]             ; Fully-featured Leiningen plugin to run a VimClojure server for Lein 1 & 2 projects.
   ;[appengine-magic "0.5.0"]          ; FIXME Abstract away the infra nuts and bolts of writing a Clojure application for GAE
   ;[lein-httpd "1.0.0"]               ; Start a web server in the current directory.
   [lein-ring "0.8.3"]                 ; Automates Ring tasks, fire up a dev server, turn a handler into standard war file.
   ;[lein-swank "1.4.4"]               ; Server that allows SLIME (the Superior Lisp Interaction Mode for Emacs) to connect to Clojure projects.
   
   ;; Mobile platforms
   ;;
   [lein-droid "0.0.6"]                ; Plugin for easy Clojure/Android development and deployment.
   
   ;; Clients
   ;;
   [clj-http "0.6.5"]                  ; An idiomatic clojure http client wrapping the apache client.
   [org.clojars.kriyative/clj-http-client "1.0.0"]
   
   ;; IDE Integrated Development Environments
   ;;
   [lein-catnip "0.5.1"]               ; A web based and Clojure IDE providing a fully functional text editor and REPL environment for CLJS.
   [lein-light "0.0.24"]               ; LightTable Playground plugin to connect to the IDE.
   [org.clojars.scott/lein-nailgun "1.1.0"] ; A leiningen plugin to launch a vimclojure nailgun server.
   
   ;; Web Domain Specific
   ;;
   [lein-cssgenbuild "0.2.1"]          ; Generate stylesheets from cssgen s-expressions.
   [hiccup-bootstrap "0.1.1"]          ; Generate stylesheets from cssgen s-expressions for Twitter Bootstrap.
   [clojurewerkz/urly "1.0.0"]         ; <url:#urly>Unifies parsing of URIs, URLs & the likes such as relative href values in real-world HTML.
   
   
   ;; TDD, code coverage, integration, benchmarking, debugging
   ;;
   [lein-cloverage "1.0.2"]            ; A Leiningen plugin to run cloverage test coverage.
   [lein-midje "3.0.0"]                ; Leiningen plugin for Test Driven Development (TDD) with Midje.
   ;[lein-guzheng "0.4.5"]             ; FIXME (hangs) Performs branch coverage analysis on your project
   ;[perforate "0.3.0"]                ; Painless benchmarking with Leiningen and Criterium.
   ;[lein-difftest "2.0.0"]            ; Run tests with better, alternate test and error reporting. 
   ;[clj-stacktrace "0.2.4-SNAPSHOT"]  ; A library for creating more readable stacktraces in Clojure programs.
   
   ;; Creative entertainment
   ;;
   [overtone "0.8.1"]                  ; Programmable collaborative music studio online
   
 
   
   
   

   ]

  :search-page-size 60
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
;id=ex-leinexec
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


