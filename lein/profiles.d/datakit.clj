;;; ~/.lein/profiles.clj

;; You should really <url:#readme> *(in vim use :Utl on this link)
;; The :plugins [[vector]] of my :user profile.clj file

{:datakit
 {:plugins 
  [
   ;; Math, statistics and probability
   ;;
   [org.clojure/math.combinatorics "0.0.3"]
   
   ;; Data, sets, collections and groups
   ;;
   [org.clojure/data.json "0.2.2"]  ; Clojure JSON library
   [edw/ordered "1.3.2"]            ; Pure implementation of ordered hash-sets
   [lein-embongo "0.2.0"]           ; Run 'embedded' instances of MongoDB
   [jackknife "0.1.2"]              ; Tools for Cascalog, ElephantDB, Storm etc.
  
   ;; Visualize
   ;;
   [lein-depgraph "0.1.0"]          ; Iterate & visualize project namespace tree
   [bigml/histogram "3.1.0"]        ; Streaming Parallel Decision Trees
   
  ]
          
}}


