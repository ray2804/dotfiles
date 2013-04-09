;;; ~/.lein/profiles.d/server.clj
;;;
;;; Server side solutions, plugins, packages and libraries infrastructure.



{:server 
 {:plugins
    [
     [lein-shell "0.1.0"]           ; Leiningen plugin for calling shell commands.
     
     ;; Servers / REPL
     ;;
     [lein-httpd "1.0.0"]           ; Start a web server in the current directory.
     [lein-swank "1.4.0"]           ; Launch a swank server to connect emacs to.
     [lein-ring "0.8.3"]            ; Automates Ring tasks, fire up a dev server, turn a handler into standard war file.
     [lein-tarsier "0.10.0"]        ; Fully-featured Leiningen plugin to run a VimClojure server for Lein 1 & 2 projects.
     ;[appengine-magic "0.5.0"]     ; FIXME Abstract away the infra nuts and bolts of writing a Clojure application for GAE.
     [lein-swank "1.4.4"]           ; Server that allows SLIME (the Superior Lisp Interaction Mode for Emacs) to connect to Clojure projects.
     
     ;; Clients
     ;;
     [clj-http "0.6.5"]             ; An idiomatic clojure http client wrapping the apache client.
     [org.clojars.kriyative/clj-http-client "1.0.0"]
     
     ;; Support
     ;;
     [clojurewerkz/urly "1.0.0"]    ; <url:#urly>Unifies parsing of URIs, URLs & the likes such as relative href values in real-world HTML.
     
   ]

}}


