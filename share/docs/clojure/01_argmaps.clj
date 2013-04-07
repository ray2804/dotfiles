;;;;    ;;   __________-------   ;;;;;;\
;; Tip #1 Sort a map on multiple keys ;;
;; ---------------------------------- ;;
;;  ...... ,,   , ;;    .....   ;; ;;;;;          


;; A      [vector]  of  _____________{maps}___________
;;            \        /       |            |         \
(def some-maps [{:x 1 :y 2} {:x 2 :y 1} {:x 4 :y 2} {:x 3 :y 8}])

;; Sort the maps first on :x and then on :y
(defn sort-maps-by
  "Sort a sequence of maps (ms) on multiple keys (ks)"
  [ms ks]
  (sort-by #(vec (map % ks)) ms))


(sort-maps-by some-maps [:x :y])


(def board {:game {:dice () :players 2}})

(defn 
  ^{:arglist "(create-dice [coll flags]) "
    :doc "Creates a new dice of argument"}
  create-dice-1 [values argmap]
 
  (println "values:"   values
           "sorted:"   (:sorted argmap 0)   ; sort sequence after
           "priori:"   (:priori argmap 1)   ; prio over other dices
           "queued:"   (:queued argmap 0)   ; queuing dices
           "attach:"   (:attach argmap 0)   ; to a specific game
           "method:"   (:method argmap 0)   ; dice roll function
           "random:"   (:random argmap 1)   ; always random sequence
           "groups:"   (:groups argmap 0)   ; type specifics of dice
           "revert:"   (:revert argmap 0)   ; reversed input of sequence
           "vector:"   (:vector argmap [])  ; takes form and outputs vector
           "seeded:"   (:seeded argmap 0)   ; uses additional seed for random
           ) ; close optional arguments
  
  ;; type checking? only checks for keys contained currently
  (println "sorted="   (if (contains? argmap :sorted) (sort values))     ; true if found
           "random="   (if (contains? argmap :random) (shuffle values))  ; true if random
           "revert="   (if (contains? argmap :revert) (reverse values))
           "vector="   (if (contains? argmap :vector) (apply vector values))
           "attach="   (if (contains? argmap :attach) #(assoc argmap :attach)) ; true if to hook
           "isaseq?"   (seq? values) (first values) (rest values) (cons :D1 values)
  ))     

  (def x= "a")
  (println x=)
  
;; testing
(create-dice-1 
 '(1 2 3 4 5) 
  {:attach board 
   :random 'yes 
   :sorted 'yes 
   :revert 'no-revert 
   :vector []})


(defn 
 ^{:arglist '([seq & args])
   :doc "Creates a new dice from any sequence. Applies rules/formatting as desired using keys:
   :vector :sorted :random :revert :groups :queued :priori :seeded and :attach to hook
   the dice to a specific game. With :method you may hook a method for rolling the dice and
   which value(s) can be thrown."}  
  create-dice-2 [values & args]
 
 ;; local variables in lexical scope, flat map
 (let [argmap (apply hash-map args)
       {:keys [bar baz]
        :or   {bar 0 baz 0}} argmap]
   
   (println "values:" values
            "bar:" bar
            "baz:" baz)
   (println "bar-given?" (contains? argmap :bar)
            "baz-given?" (contains? argmap :baz))))

(named-args-2 1 :baz 2)











;; A three-dimensional shape that is made up of a finite number of polygonal faces 
;; which are parts of planes; the faces meet in pairs along edges which are straight-line 
;; segments, and the edges meet in points called vertices.
(def polyhedra 
  "Regular Polyhedra: each face being a regular polygon (all sides and angles equal) 
  and with each vertex (corner) having an equal arrangement and number of faces, all 
  at equal angles. Make great dice because each face has an equal chance of being thrown 
  as any other and can be considered as fair dice."
  
  {:tetra  {:name "Tetrahedron"  :edges 6  :faces 4  :vertices 4  :shape "equilateral triangle"} 
   :hexa   {:name "Hexahedron"   :edges 12 :faces 6  :vertices 8  :shape "perfect square"} ;; Opposite faces usually add up to 7
   :octa   {:name "Octahedron"   :edges 12 :faces 8  :vertices 6  :shape "equilateral triangle"} ;; Opposite faces usually add up to 9.
   :dodeca {:name "Dodecahedron" :edges 30 :faces 12 :vertices 20 :shape "pentagon"}
   :icosa  {:name "Icosahedron"  :edges 30 :faces 20 :vertices 12 :shape "equilateral triangle"}})

;; TODO Add formulas etc.


(defn 
  ^{:arglist '([values & ks])
    :doc "Create a flat map of optional arguments keys and values. Allows clean and complex method
    definitions to easily extended in functionality and focused on usability for programmer and user."}
  create-dice-3 [values & {:keys [sorted queued attach method random groups revert arrays seeded shaped]
                           :or   {sorted true 
                                  queued false 
                                  attach nil 
                                  method nil 
                                  groups {:geometry "Polyhedral" :shape "c"}
                                  shaped true ;; normal 6 faced dice
                                  revert (reverse values)
                                  
                                 }
                          :as   argmap}]

  (println "values:" values
           "sorted:" sorted
           "queued:" queued
           "attach:" attach
           "method:" method
           "random:" random
           "groups:" groups
           "revert:" revert
           "arrays:" arrays
           "seeded:" seeded
           
           )
  
 (println "bar-given?" (contains? argmap :sorted)
          "baz-given?" (contains? argmap :queued)))


(create-dice-3 '(1 2 3 4) :sorted true :revert true)















;; Tip #2
;; When dealing with infinite sequences on the REPL, you can set the number of items to be printed:
;;; When you type something like (iterate inc 1) on the REPL (or any
;;; kind of infinite, lazy sequence) the REPL will try to evaluate the
;;; whole thing and will never finish. One way to print some parts of
;;; an infinite sequence on the REPL is to do this on the REPL and
;;; then try to print the sequence -
;;; (set! *print-length* 10)
;;; (iterate inc 1)
;;; Which will only print the first 10 items of the above infinite
;;; sequence -
;;; (1 2 3 4 5 6 7 8 9 10 ...)
;;; There is also *print-level* which can be used to determine how
;;; nested/recursive data-structures are printed on the REPL


;(ns tips
  ;; requires clojure 1.2 if you are on 1.1.x, use this instead
  ;; (:require [clojure.contrib.duck-streams :as io])
;  (:require clojure.java.io :as io]))

;;; Tip #3
;;; Use of the -> & ->> threading macros.
;(defn word-freq
;  "Calculate a frequency map of words in a text file."
;  [f]
;  (take 20 (->> f
;                read-lines
;                (mapcat (fn [l] (map #(.toLowerCase %) (re-seq #"\w+" l))))
;                (remove #{"the" "and" "of" "to" "a" "i" "it" "in" "or" "is"})
;                (reduce #(assoc %1 %2 (inc (%1 %2 0))) {})
;                (sort-by (comp - val)))))

;;; Run it like this (word-freq "/path/to/file.txt")