;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.

(class (get [0 0 1 2 3 0] 2))

(pos? (get [0 0 0 1 1 -1] 4))

(assoc [0 1 2 3 4] 2 :two)

(subvec [0 1 0 0 2 0 0 0 1 0 0 2 0 0 0 0 0] 2 6)

(defn max?
  "Checks to see if a field (pigeonhole) is filled to the max."
  [num]
  (if
    (and (< num 5)
         (> num -5))
    true false))

(max? -5)

(defn occ?
  "Checks to see if a field (pigeonhole) is filled to the max."
  [num]
  (if
    (and (< num 5)
         (> num -5))
    true false))



(def board
  (vec (repeat 24 0)))

(repeatedly 10 #(rand-nth (range -5 +5)))

(assoc board 2 2)



{
 :board (vec (repeat 24 0))
 :index (vec (doall (range 0 (inc (count board)))))
 :valid (range -5 +5)
 }




