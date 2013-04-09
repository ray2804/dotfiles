;;; ~/.lein/profiles.d/webdsl.clj   ;; Web Domain Specific
;;;
;;; Domain Specific Languages (DSL) for web development in Clojure
;;; and Leiningen. URL parsing and translations or compilation to
;;; HTML, CSS and JavaScript.
;;;
;;; TODO: Find web 3.0 tool for vocabularies
;;;

{:webdsl
 {:plugins 
  [
   [lein-cssgenbuild "0.2.1"]          ; Stylesheets
   [hiccup-bootstrap "0.1.1"]          ; Twitter Bootstrap
   [clojurewerkz/urly "1.0.0"]         ; Unifies parsing of URIs
   [lein-aggravate "0.1.0-SNAPSHOT"]   ; Concatenate and compress
  ]
 }
}
 ""
   
   