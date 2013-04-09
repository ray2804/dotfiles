;;; ~/.lein/profiles.d/documentate.clj
;;;
;;; Project documentation, literate programming and paper/site publishing.
;;; 
;;; Contents:
;;; 1. Marginalia   Ultra-lightweight literate programming tool
;;; 2. Codox        Generate API documentation from source code
;;;


{:documentate {:plugins [ [lein-marginalia "0.7.1"]
                          [codox "0.6.4"]]}}
