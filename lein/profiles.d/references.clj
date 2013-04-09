;;; ~/.lein/profiles.d/references.clj
;;;
;;; Project documentation, literate programming and site/documents publishing.
;;; Reference sources and management of information, writing and quality assurance.
;;;

{:references
 {:plugins 
    [
       [org.clojure/data.json "0.2.2"]    ; Clojure JSON library
       [lein-pprint "1.1.1"]              ; Pretty-print a representation of the project map.
       
       ;; Literate programming and documentation
       ;;
       [lein-marginalia "0.7.1"]           ; Ultra-lightweight literate programming for Clojure and ClojureScript.
       [codox "0.6.4"]                     ; A tool for generating API documentation from Clojure source code.
       
    ]

  }}
