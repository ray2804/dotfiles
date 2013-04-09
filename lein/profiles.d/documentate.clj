;;; ~/.lein/profiles.d/documentate.clj
;;;
;;; Project documentation, literate programming and site/documents publishing.
;;;

{:documentate
 {:plugins 
    [
       ;; Literate programming and documentation
       ;;
       [lein-marginalia "0.7.1"]    ; Ultra-lightweight literate programming
       [codox "0.6.4"]              ; Generating API documentation from source  
    ]}}
