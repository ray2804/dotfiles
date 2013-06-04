(ns net.solobit.games.foo
  (:refer-clojure :exclude [extend])
  (:require [clj-time.core]))

;; A die (plural dice, which can also be used as the singular, from French dé, from Latin datum "something which is given or played") is a small throwable object with multiple resting positions, used for generating random numbers. This makes dice suitable as gambling devices for games like craps, or for use in non-gambling tabletop games.

;; An example of a traditional die is a rounded cube, with each of its six faces showing a different number of dots (pips) from 1 to 6. When thrown or rolled, the die comes to rest showing on its upper surface a random integer from one to six, each value being equally likely.

(def dice1 (apply vector (range 1 7)))
(doall dice1)
;(defn random-pip (rand-nth dice1)) ;<= needs a vector
(type dice1)
(rand-nth dice1)

;; A variety of similar devices are also described as dice; such specialized dice may have polyhedral or irregular shapes and may have faces marked with symbols instead of numbers.
(defmacro synonym [s s′]
  (list 'defmacro s′ (str s) '[& rest]
     (list 'cons (list 'quote s) 'rest)))

(synonym 'comment todo)
(synonym 'comment prio)

(todo "extra dices")

;; Dice have been used throughout Asia since before recorded history; the oldest known dice were excavated as part of a 5000-year-old backgammon set at the Burnt City, an archeological site in south-eastern Iran.

;; Gambling with two or three dice was a very popular form of amusement in Greece, especially with the upper classes, and an invariable accompaniment to symposia.

;; extremely naive / simplistic
(defn roll-dice-proto [qty]
  (let [r1 #(rand-nth (apply vector (range 1 7))) ; 3 function calls but you might need this
        r2 #(inc (rand-int 6)) ; starts at 0 thus increment value with +1
        d1 (r1) ; you could use local variables but might as well have the body return
        d2 (r2)]
    (println (repeatedly qty (r1)))
    (println d1)
    (println d2)
    (println (now))))

(roll-dice 2)

(defn roll-dice [qty]
  "Quantity refers to the amount of dice thrown in 1 roll.
  Some games have you roll 1, others 2 or 3 dice at once."
  (let [f #(inc (rand-int 6))]
    (repeatedly qty f)))

(doall (dice-outcomes 20))








(defn roll-dice [qty]
  "Quantity refers to the amount of dice thrown in 1 roll.
  Some games have you roll 1, others 2 or 3 dice at once."
  (let [f #(inc (rand-int 6))]
    (repeatedly qty f)))

(comment "Some may require you to sort the dice.")

(sort (roll-dice 2))

(comment "Which in turn, may form pairs or tuples, sorted or not.")

;; Lazy sequence, use doall to evaluate

(doall (repeatedly 100 #(sort (roll-dice 2))))

(defn tt-generator
  [pairs freq]
  (let [drl #(sort (roll-dice pairs))
        seq (repeatedly freq #(drl))
        mod #(when (= %1 '(1 2)) "foo")]
    (reduce #(assoc %1 %2 (inc (%1 %2 0)))
     {} seq)))

(tt-generator 2 1000)


