
;; Secure temporary file creation
(doto (java.io.File/createTempFile "pre" ".suff") .deleteOnExit)

;; Sum of a series
(reduce + (map #(/ 1.0 % %) (range 1 1001)))

;; Repetetions of random pick from vector
(def t1 (gen/reps 50 #(gen/rand-nth [1 2 3 4 5 6])))


;; You want to accumulate some changes
;;
;; Lazy sequence of numbers in a range. 
;; First two get f applied, then the rest till nothing
(range 1 4)
(reduce + (range 1 4))
;; look good again and make sure you get it
(range 1 10)
(range 2 20 2)
(range 1 9 3)
(reduce + (range 4 12 2))
(reduce * (range 1 20))
(range 010 200 20)

(number? 8)








(map even? (range 1 10))


;(first (sort (repeatedly 2 #(gen/rand-nth [1 2 3 4 5 6]))))
;(pprint (group-by inc (sort t1)))
;(println (partition-by even? t1))

;; split on first failure of predicate
(def fail-at-one (split-with even? [1 2 4 3 5 6]))
(def fail-at-two (split-with odd? [1 2 3 4 5 6]))