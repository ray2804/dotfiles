(ns closhard.core
   (:require [swiss-arrows.core :refer :all]))



(defmacro synonym [s s′]
  (list 'defmacro s′ (str s) '[& rest]
     (list 'cons (list 'quote s) 'rest)))

(synonym defmacro ≜m)
(synonym defn ≜λ)
(synonym def ≜)
(synonym fn λ)
(synonym letfn letλ)



(def game
  (ref {:stack 15
        :board [[1 2 0 0 0 0]
                [0 0 0 0 0 0]
                [0 0 0 0 0 0]
                [0 0 0 0 0 0]]}))

(defn can-move? [a b]
  "Checks if point a may move to point b."
  (if (or (< 1 a) (> 1 b)) false true))


(defn occupied? [d]
  (if (zero? d) true false))

(defn p1? [x] (pos? x))
(defn p2? [x] (neg? x))



;; keep some easy-to-use refs to the core arithmetic system
(def c+ clojure.core/+)
(def c- clojure.core/-)
(def c* clojure.core/*)
(def c-div clojure.core//)

(defn type-dispatch [& rest]
  (let [[x y] rest]
    [(type x) (type y)]))

;;------------------------------------------------------------------------------
;; Addition

(defmulti + type-dispatch)

(defmethod + [nil nil] [& rest] 0)
(defmethod + [java.lang.Number nil] [x] x)
(defmethod + [java.lang.Number java.lang.Number] [x y & rest]
   (reduce + (c+ x y) rest))




(defn owns?
  [x]
  (if (pos? x) "p1" "p2"))

(owns? 1)

(defn alter-ref [ref type op]
  {:pre [(or (= op inc) (= op dec) (nil? op))]
   :post [(and (= type :stack) (< 15))]}
  "change a map value with key type stored in ref"
  (dosync
   (alter ref assoc type (op (type @ref)))
   (type @ref)))


(defn foo [a] a)
(= :bar (foo :bar))
(alter-ref game :stack inc)







(defn p1? [x]
  (when (pos? x) true))

(defn p2? [x]
  (when (pos? x) false))

(defn occupiable? [x]
  (when (zero? x)))

(defn hittable? [x]
  (when (or (= x -1) (= x 1)) true))






(defn move-stones [a b]
  "Move stones from position a to b."

  ;; Make the references more natural, no 0's
  (let [from-sector (dec (first a))
        from-field (dec (second a))
        to-sector (dec (first b))
        to-field (dec (second b))]

    (str (((:board @game) from-sector) from-field)
         " to "
         (((:board @game) to-sector) to-field)

         )))

(move-stones [1 1] [2 2])


(def test-1 [[2 3 2 4 4 0] [1 2 0 0 0 0] [0 0 0 0 0 0] [0 0 0 0 0 0]])

(def test-3 [[0 0 0 0 0 0] [0 0 0 0 0 0] [0 0 0 0 0 0] [0 0 0 0 0 0]])
(def test-4 [[0 0 0 0 0 0] [0 0 0 0 0 0] [0 0 0 0 0 0] [0 0 0 0 0 0]])
(def test-5 [[0 0 0 0 0 0] [0 0 0 0 0 0] [0 0 0 0 0 0] [0 0 0 0 0 0]])

