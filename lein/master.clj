(ns master.exampler)

(cycle ['a 'b 'c 'd])

;; conditional
(if (= 0 0) true false) ; will error if no return values given
(when (= true true)) ; can just handle missing as nil

(let [grade 85]
  (cond
   (>= grade 90) "A"
   (>= grade 80) "B"
   (>= grade 70) "C"
   (>= grade 60) "D"
   :else "F"))

;; Generates a random number compares it to user input
(let [rnd (rand-int 10)
      guess (Integer/parseInt (read-line))]
  (cond
   (= rnd guess) (println "You got my guess right!")
   :else (println "Sorry... guess again!")))


;;
;; Looping constructs
;;

(defn my-zipmap
  [keys vals]
  (loop [my-map {}
         my-keys (seq keys)
         my-vals (seq vals)]
    (if (and my-keys my-vals)
      (recur (assoc my-map (first my-keys) (first my-vals))
             (next my-keys)
             (next my-vals))
      my-map)))

(my-zipmap [:a :b :c] [1 2 3])

(defn factorial
  [n]
    (loop [cnt n acc 1]
       (if (zero? cnt)
            acc
          (recur (dec cnt) (* acc cnt)))))

