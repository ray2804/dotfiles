(ns dos)

;; abbr dz doz
;; History: every nuckle per hand 12 * 12 = 144
;; one hand for counting, the other as counter (no aid)

(def dozen 12)
(def half-dozen (/ dozen 2))
(def two-dozen (* dozen 2))

half-dozen
two-dozen

;; Decimal, 10
;; History: humans have 10 fingers
(def decimal 10)


;; Since ancient times, mathematicians have been fascinated by problems
;; concerning prime numbers, and many people have worked on the problem
;; of determining ways to test if numbers are prime. One way to test if
;; a number is prime is to find the number’s divisors.

(defn divides? [a b & rest]
  (= (rest b a) 0))

(defn square [x] (* x x))
(defn find-divisor [n test-divisor]
  (when ((> (square test-divisor) n) n)
        ((divides? test-divisor n) test-divisor)
        :else (find-divisor n (+ test-divisor 1))))

(defn smallest-divisor [n]
  (find-divisor n 2))


;; We can test whether a number is prime as follows: n is prime if and only
;; if n is its own smallest divisor.

(defn prime? [n]
  (= n (smallest-divisor n)))

prime? 10