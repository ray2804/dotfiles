;;; ~/.lein/profiles.clj

;; A test-driven and benchmarking profile for Leiningen 2 Clojure development
;; and debugging.

{:tdd {:dependencies [
                       [midje "1.4.0"] 
                       ; Test framework for facts: truthy or falsey (see lein-midje)
                      ]
        :plugins [
   
                  ;; Servers and REPL
                  ;;
                  [lein-swank "1.4.0"]                
                  ; Launch a swank server to connect emacs to.
                  
                  [lein-httpd "1.0.0"]                
                  ; Start a web server in the current directory.
                  
                  [lein-ring "0.8.3"]                 
                  ; Automates Ring tasks, fire up a dev server, turn a handler into standard war file.
                  
                  [lein-swank "1.4.4"]                
                  ; Server that allows SLIME (the Superior Lisp Interaction Mode for Emacs) to connect to Clojure projects.
                  
                  ;; TDD, code coverage, integration, benchmarking, debugging
                  ;;
                  
                  [lein-cloverage "1.0.2"]            
                  ; A Leiningen plugin to run cloverage test coverage.
                  
                  [lein-midje "3.0.0"]                
                  ; Leiningen plugin for Test Driven Development (TDD) with Midje.
                  
                  ;[lein-guzheng "0.4.5"]             
                  ; FIXME (hangs) Performs branch coverage analysis on your project.
                  
                  ;[perforate "0.3.0"]                
                  ; Painless benchmarking with Leiningen and Criterium.
                  
                  ;[lein-difftest "2.0.0"]            
                  ; Run tests with better, alternate test and error reporting. 
                  
                  ;[clj-stacktrace "0.2.4-SNAPSHOT"]  
                  ; A library for creating more readable stacktraces in Clojure programs.

                  ]
        :resource-paths ["diagnostics" "test-data" "report-out"]
        
        }    }


