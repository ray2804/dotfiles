{:user {
        :dependencies [[org.clojure/clojure "1.5.1"]]

        :plugins [[lein-ancient "0.4.4"]

                  ]

        }
 }


;;[slothcfg "1.0.1"] dont use in global
;                       [org.clojure/jvm.tools.analyzer "0.4.3"]
;                       [garden "0.1.0-beta6"]
;                       [prismatic/plumbing "0.1.0"]
;                       [inflections "0.8.2"]

;; {:user {:plugins
;;         [
;;          ;; ### LEIN-CLJSBUILD
;;          ;; <https://github.com/emezeske/lein-cljsbuild>

;;          ;; Makes it quick and easy to automatically compile your ClojureScript
;;          ;; code into Javascript whenever you modify it. It's simple to install
;;          ;; and allows you to configure the ClojureScript compiler from within
;;          ;; your project.clj file.
;;          ;[lein-cljsbuild "0.3.2"]

;;          ;; ### LEIN-PPRINT
;;          ;; <https://github.com/technomancy/leiningen/tree/master/lein-pprint>

;;          ;; Pretty-print a representation of the project map. Use this with e.g.
;;          ;; different profiles for categories of projects, or any other way you
;;          ;; like to organize them: `$ lein with-profile myprofile test :database`
;;          [lein-pprint "1.1.1"]

;;          ;; ### LEIN-PDO
;;          ;; <https://github.com/Raynes/lein-pdo>

;;          ;; Running multiple tasks in parallel. This is primarily useful for
;;          ;; running multiple tasks where one or more would normally block
;;          ;; forever, preventing future tasks from executing.
;;          ;[lein-pdo "0.1.1"]

;;          ;; ### LEIN-SUB
;;          ;; <https://github.com/kumarshantanu/lein-sub>

;;          ;; Help for projects that may have sub-projects (each having its own
;;          ;; project.clj file) - pull dependencies, install, create jars for them
;;          [lein-sub "0.2.4"]

;;          ;; ### LEIN-GREP
;;          ;; <https://github.com/cldwalker/lein-grep>

;;          ;; A Leiningen plugin that renders meaningful search results.
;;          ;[lein-grep "0.1.0"]


;;          [lein-clique "0.1.0"]

;;          ;; ### LEIN-ANCIENT
;;          ;; <https://github.com/xsc/lein-ancient>

;;          ;; Check your project for outdated dependencies and plugins, uses
;;          ;; metadata XML files in the different Maven repositories instead
;;          ;; of a Lucene-based search index. Version comparison is done using
;;          ;; version-clj.
;;          [lein-ancient "0.4.4"]

;;          ;; ### LEIN-MARGINALIA
;;          ;; <https://github.com/gdeer81/lein-marginalia>

;;          ;; Ultra-lightweight literate programming for Clojure and ClojureScript
;;          ;; inspired by docco. Marginalia is a source code documentation tool that
;;          ;; parses Clojure and ClojureScript code and outputs a side-by-side source
;;          ;; view with appropriate comments and docstrings aligned.
;;          [lein-marginalia "0.7.1"]

;;          ;; ### LEIN-AUTORELOAD
;;          ;; <https://github.com/pyronicide/lein-autoreload>

;;          ;; Guarantees when running the repl, your files when changed are updated
;;          ;; and will output e.g. something like `:reloading (my-proj.core)`
;;          ;[lein-autoreload "0.1.0"]

;;          ;; ### LEIN-BIKESHED
;;          ;;
;;          ;;
;;          [lein-bikeshed "0.1.3"]   ;; notify code smell or bad practice that should make you feel bad

;;          ]

;;         ;; user
;;         :dependencies
;;         [
;;          ;; temp

;;          ;[robert/hooke "1.3.0"]
;;          ;[me.raynes/fs "1.4.5"]

;;          ;; ### PEDESTAL
;;          ;; <pedestal.io>
;;          ;;
;;          ;; Next gen web development environment.
;; ;;          [pedestal-service/lein-template "0.1.10"]
;; ;;          [io.pedestal/pedestal.app "0.1.10"]
;; ;;          [io.pedestal/pedestal.service "0.1.10"]
;; ;;          [io.pedestal/pedestal.jetty "0.1.10"]

;;          ;; ### DOMINA
;;          ;; <>
;;          ;;
;;          ;; HTML DOM manipulation library
;;          ;[domina "1.0.1"]

;;          ;; CLOJURE-NLP
;;          ;; <>
;;          [clojure-opennlp "0.3.1"]

;;          ;; ### FLUOKITTEN
;;          ;; <https://github.com/uncomplicate/fluokitten>
;;          ;; <http://fluokitten.uncomplicate.org/>

;;          ;;
;;          [uncomplicate/fluokitten "0.3.0"]

;;          ;; ### FRAK
;;          ;; <https://github.com/noprompt/frak>

;;          ;; Frak transforms collections of strings into regular expressions for
;;          ;; matching those strings. The primary goal of this library is to
;;          ;; generate regular expressions from a known set of inputs which avoid
;;          ;; backtracking as much as possible.
;;          [frak "0.1.3"]

;;          ;; ### INFLECTIONS
;;          ;; <http://github.com/r0man/inflections-clj>

;;          ;; Plural case of English words. (singular word) (singular word)
;;          [inflections "0.8.2"]

;;          ;; ### DIRE
;;          ;; <https://github.com/MichaelDrogalis/dire>

;;          ;; Erlang-like supervise functions and error handlers which nicely
;;          ;; take the exception handling from the elegant functions you've written
;;          ;; and allows to define (eager) pre-/post-handlers to contain the often
;;          ;; very defensive parts of coding.
;;          [dire "0.4.4"]

;;          ;; ### HICCUP
;;          ;; <https://github.com/weavejester/hiccup

;;          ;; Excellent HTML DSL for Clojure, I use it all the time. HTML elements
;;          ;; are represented as vectors with as second item, a map of attributes
;;          ;; and finally the text value e.g. [:div [:p {:id "1"} "Hello!"]]
;;          ;[hiccup "1.0.4"]

;;          ;; ### BACKTICK
;;          ;; <https://github.com/brandonbloom/backtick>

;;          ;; Instant classic, this wonderful tool helps me write syntax-quoted
;;          ;; Clojure easily and has templates/custom quotes to enable symbol var
;;          ;; namespace resolution, so you can use them with gensym. Sweet.
;;          [backtick "0.3.0"]

;;          ;; ### HTTP-KIT
;;          ;; <http://http-kit.org/>

;;          ;; Solid web server tested to remain functioning while taking care of a
;;          ;; whopping 600.000 concurrent requests. Nice. Usually a lot of solutions
;;          ;; are packed with Jetty, overall it's a tad better supported due to the
;;          ;; long history in Java, Http-kit is a very worthy contender though.
;;          [http-kit "2.1.10"]

;;          ;; ### SWISS-ARROWS
;;          ;; <https://github.com/rplevy/swiss-arrows>

;;          ;; Very nice extra monads for Clojure besides the core -> ->> threading
;;          ;; macros, these contain e.g. -<> -<>? and so on, allow for subpaths and
;;          ;; choice *where* to have your monads pipe through for example:
;;          ;; `(-<> "foo" (str <> " and you"))` diamand `<>` is where its piped.
;;          [swiss-arrows "0.6.0"]

;;          ;; ### TOOLS.NAMESPACE
;;          ;; <https://github.com/clojure/tools.namespace>
;;          ;; <stuartsierra.com/2013/03/29/perils-of-dynamic-scope>

;;          ;; Written by Stuart Sierra, a must to master, check the article! Make a
;;          ;; project, better said a project in which all state is contained in
;;          ;; a single var, where you've abolished all global state from the app
;;          ;; and you can easily load, refresh, start and stop the entire
;;          ;; configuration.
;;          ;[org.clojure/tools.namespace "0.2.4"]

;;          ;; ### CLOJURE-CONTRIB
;;          ;; <https://github.com/clojure>

;;          ;; This should almost be natively built-in, a lot of it probably will
;;          ;; at some point fall under clojure.core so keep these in mind, you
;;          ;; will probably need this at some point. This used to be a single
;;          ;; monolythic library, it's now more properly devided into several
;;          ;; packages with their own maintainers.
;;          [org.clojure/clojure-contrib "1.2.0"]

;;          ;; ### ALGO.MONADS
;;          ;; <https://github.com/clojure/algo.monads>
;;          ;[org.clojure/algo.monads "0.1.4"]

;;          ;; ### ITSY
;;          ;; <https://github.com/dakrone/itsy>
;;          ;;
;;          ;; A threaded web spider, written in Clojure.
;;          ;;
;;          ;; WARNING: SLF4J CONFLICT
;;          ;[itsy "0.1.1"]



;;          ]} ; /user


;;  ;; ## Web development
;;  ;;
;;  ;; This profile better supports work on web projects, mostly on the development
;;  ;; side for things on the front-end like HTML, CSS stylesheets or other DSL and
;;  ;; templating, build and publishing tools related to the world wide web.
;;  :webdev {:plugins
;;           [
;;            ;; ### MANGOES (oh-noes!) ~ FIXME (development work)
;;            ;; <https://github.com/murtaza52/lein-mangoes>

;;            ;; A build system for client side development, simply add a keyword
;;            ;; to your project map `:mangoes` which defines the different folders
;;            ;; to watch and the action to take, e.g.
;;            ;;   :mangoes [[:hiccup->html "app/hiccup-templates" "app/templates"]
;;            ;;             [:html->hiccup "app/tmp" "app/hiccup-templates"]]
;;            ;; The watch can then be started using: $ lein mangoes
;;            ;; Create the folders hiccup-templates and templates in your project.
;;            ;; cd into the hiccup-templates folder.
;;            ;; Create a hiccup file : echo "[:a [:b]]" >> a.clj
;;            ;; This will produce a html file : cat ../templates/a.html
;;            ;; >> <a><b></b></a> (does break easily). CSS Garden not implemented yet.
;;            [lein-mangoes "0.2.1"]

;;            ;; ### LEIN-SIMPLETON
;;            ;; <https://github.com/tailrecursion/lein-simpleton>

;;            ;; Serve files out of a local directory. By default Simpleton provides
;;            ;; a file-server in the directory where it's run. To run on port 5000:
;;            ;; `$ lein simpleton 5000`.
;;            ;; Click around to navigate directories and download (some) files. If a
;;            ;; directory contains a file named either index.html or index.htm then
;;            ;; Simpleton will attempt to serve that automatically.
;;            [lein-simpleton "1.1.0"]

;;            ;; ### LUMINUS
;;            ;; <http://www.luminusweb.net>

;;            ;; Simply use the following command, luminus template is built-in
;;            ;; with leiningen so once you've got that down simply issue:
;;            ;; $ lein new luminus <your project name>
;;            ;; [nil]

;;            ;; ### LEIN-CLJSBUILD
;;            ;; <https://github.com/emezeske/lein-cljsbuild>

;;            ;; Makes it quick and easy to automatically compile your ClojureScript
;;            ;; code into Javascript whenever you modify it. It's simple to install
;;            ;; and allows you to configure the ClojureScript compiler from within
;;            ;; your project.clj file.
;;            [lein-cljsbuild "0.3.2"]



;;            ]
;;           :dependencies
;;           [
;;            ;; ### JTIDY (org.w3c.tidy)

;;            ;; Mangoes e.g. doesn't check for valid HTML and you can easily break
;;            ;; any conversion from html->clj. One solution to this problem of say
;;            ;; malformed or invalid HTML, as well as pretty printing it, is to use
;;            [jtidy "4aug2000r7-dev"]

;;            ]

;;           } ; /webdev


;;  ;; ## Analyze
;;  ;;
;;  ;; The `analyze` profile, for the most of it, contains several handy pieces of
;;  ;; software packages, functions and libraries to analyze your code, visualize
;;  ;; or otherwise provide useful (debugging) information about a project in terms
;;  ;; of metrics, graph generation, reports, statistics, enhanced stack traces
;;  ;; and such.
;;  :analyze {:plugins
;;           [
;;            ;; ### LEIN-CLIQUE
;;            ;; <https://github.com/Hendekagon/lein-clique>

;;            ;; lein-clique goes through your source code to find which functions
;;            ;; external to a function's namespace it depends on, then generates
;;            ;; a graphviz graph of those dependencies. If you analyze the graph,
;;            ;; you can find for example which functions are most used by other
;;            ;; functions (in-degree - in the Clojure core namespace these are:
;;            ;; concat, seq and list), or which functions are most dependent on
;;            ;; other functions.
;;            [lein-clique "0.1.0"]

;;            ;; ### LEIN-DEPGRAPH

;;            [lein-depgraph "0.1.0"]

;;            ;; ### LEIN-DIFFTEST
;;            ;; <https://github.com/brentonashworth/lein-difftest>

;;            ;; It works exactly the same as lein test but with alternate test
;;            ;; reporting. When a test fails, the report shows the diff of the
;;            ;; actual and expected forms. When a test fails due to an error,
;;            ;; stacktraces are cleaned up with clj-stacktrace.
;;            [lein-difftest "2.0.0"]



;;            ]
;;           :dependencies
;;           [

;;            ]

;;           }

;;  ;; ## Profile: overkill
;;  ;;
;;  ;; This profile has little provocative name, since the solutions posted here
;;  ;; almost certainly probable overkills for the problem you try to solve.
;;  :overkill {:plugins []
;;           :dependencies
;;           [
;;            ;; ### MEGAREF
;;            ;; <https://github.com/cgrand/megaref>
;;            ;; <http://clj-me.cgrand.net/2011/10/06/a-world-in-a-ref/>

;;            ;; This library introduces two new reference types (associative refs
;;            ;; and subrefs) which participate in STM transactions. Allow to deal
;;            ;; with the granularity problem even in an existing codebase without
;;            ;; changing the shape of your code. An associative ref (aka "megaref")
;;            ;; is a ref designed to allow concurrent path-keyed updates. As a
;;            ;; consequence, it can store bigger values than a ref.

;;            ]
;;             }
;;  }
