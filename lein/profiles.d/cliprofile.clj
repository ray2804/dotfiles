;;; ~/.lein/profiles.clj
;;;
;;; Shell, virtual terminal emulators, (serial) text console and printing tools
;;; for the command line interface (CLI).
;;;

{:console 
 {:plugins  
  [      
     [clojure-lanterna "0.9.3"]      ; Curses replacement library
     [lein-shell "0.1.0"]            ; Calling shell commands
     [lein-exec "0.3.0"]             ; Shell executable Clojure scripts
     [lein-bin "0.3.2"]              ; Stand-alone console execs for projects
     [me.raynes/conch "0.5.1"]       ; Low-level interfaces to Java process APIs
     [org.clojure/tools.cli "0.2.2"] ; Command line argument parser
     [lein-pprint "1.1.1"]           ; Pretty-print content of project maps
     [fipp "0.1.0-SNAPSHOT"]         ; Fast Idiomatic Pretty Printer for Clojure
  ]
       
}}

