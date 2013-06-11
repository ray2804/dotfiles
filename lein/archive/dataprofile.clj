;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; ~/.lein/profiles.d/dataprofile.clj
;;;
;;; Data, sets, collections, matrices, databases, visualization, graphing,
;;; math, groups, probability and set theory, groups and statistics.
;;;
;;; ---------------------------------------------------------------------------
;;;  #  Contents
;;; ===========================================================================
;;;
;;;  1. math.combinatorics
;;;  2. data.json           Clojure JSON library
;;;  3. ordered             Pure implementation of ordered hash-sets
;;;  4. lein-embongo        Run 'embedded' instances of MongoDB
;;;  5. jackknife           Tools for Cascalog, ElephantDB, Storm etc.
;;;  6. lein-depgraph       Iterate & visualize project namespace tree
;;;  7. histogram           Streaming Parallel Decision Trees
;;;  8. incanter            Statistical programming and data visualization
;;;  9. data.generators     Generators for random Clojure data with seeds
;;; 10. lobos               SQL db schema manipulation and migration library 
;;; 11. postgresql          Relational Database Management System like MySQL
;;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,


{:dataprofile {:plugins [ [org.clojure/math.combinatorics "0.0.3"]
                          [org.clojure/data.json "0.2.2"]
                          [edw/ordered "1.3.2"]
                          [lein-embongo "0.2.0"]
                          [jackknife "0.1.2"]
                          [lein-depgraph "0.1.0"]
                          [bigml/histogram "3.1.0"]
                          [incanter "1.4.1"]
                          [org.clojure/data.generators "0.1.0"]
                          [lobos "1.0.0-beta1"]
                          [postgresql "9.1-901.jdbc4"]]
 
               }
 }


