;;;;    ;;                       ;;;;;;\
;; Tip #1 Sort a map on multiple keys ;;
;; ---------------------------------- ;;
            ;;            ;; ;;;;;          


;; A      [vector]  of  _____________{maps}___________
;;            \        /       |            |         \
(def some-maps [{:x 1 :y 2} {:x 2 :y 1} {:x 4 :y 2} {:x 3 :y 8}])

;; Sort the maps first on :x and then on :y
(defn sort-maps-by
  "Sort a sequence of maps (ms) on multiple keys (ks)"
  [ms ks]
  (sort-by #(vec (map % ks)) ms))


(sort-maps-by some-maps [:x :y])





(defn create-dice [values argmap]
  (println "values:" values
           "sorted:" (:sorted argmap 0) ; sort sequence after
           "priori:" (:priori argmap 1) ; prio over other dices
           "queued:" (:queued argmap 0) ; queuing dices
           "attach:" (:attach argmap 0) ; to a specific game
           "method:" (:method argmap 0) ; dice roll function
           "random:" (:random argmap 1) ; always random sequence
           "groups:" (:groups argmap 0) ; type specifics of dice
           "revert:" (:revert argmap 0) ; reversed input of sequence
           "vector:" (:vector argmap []); takes form and outputs vector
           "seeded:" (:seeded argmap 0) ; uses additional seed for random
           ) ; close optional arguments
  
  ;; type checking? only checks for keys present atm
  (println "sorted?" (if (contains? argmap :sorted) (sort values))     ; true if found
           "random?" (if (contains? argmap :random) (shuffle values))  ; true if random
           "revert?" (if (contains? argmap :revert) (reverse values))
           "vector?" (if (contains? argmap :vector) (apply vector values))
           
           "attach?" (contains? argmap :attach) ; true if to hook
     
  ))     
  
(create-dice '(1 2 3 4 5) {:random 1 :sorted 1 :revert 1 :vector 1})






















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