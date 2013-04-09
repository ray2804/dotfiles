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
   [lein-cssgenbuild "0.2.1"]          ; Generate CSS stylesheets
   [hiccup-bootstrap "0.1.1"]          ; Twitter Bootstrap
   [clojurewerkz/urly "1.0.0"]         ; Unifies parsing of URIs
   [lein-aggravate "0.1.0-SNAPSHOT"]   ; Concatenate and compress
   [compojure "1.1.5"]                 ; A concise routing for Ring
   [liberator "0.9.0-SNAPSHOT"]        ; Expose data as REST resources
   [forms-bootstrap "0.2.0-SNAPSHOT"]  ; Utility for creating web forms
   [alandipert/desiderata "1.0.1"]     ; A ClojureScript bag of tricks
   [lein-webapp-template/lein-template "1.3.0"] ; Web apps based on Compojure, Stencil, Bootstrap and jQuery
   [org.clojure/data.json "0.2.2"]     ; Clojure JSON library
  ]
 }
}

   
   