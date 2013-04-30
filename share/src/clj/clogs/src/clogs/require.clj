 

;; A fraction (from Latin: fractus, "broken") represents a part of a whole or, more generally, any number of equal parts. When spoken in everyday English, a fraction describes how many parts of a certain size there are, for example, one-half, eight-fifths, three-quarters.

;; Common vulgar / fractions (natural language included in comments)
(/ 1 4)  ;; a quarter
(/ 1 3)  ;; a third
(/ 1 2)  ;; or a half
(/ 3 4)  ;; three quarter
(/ 6 12) ;; half a dozen (dz)


;; The English word dozen comes from the old form of the French word douzaine, meaning "a group of twelve"
(def dozen
  "A dozen (common abbreviated doz or dz)"
  12)

(def grouping-of-twelve
  "The dozen may be one of the earliest primitive groupings."
  (set (take dozen (range))))

;; Perhaps because there are approximately a dozen cycles of the moon or months in a cycle of the sun or year. 

;; NOTE: The average calendrical month, which is 1⁄12 of a year, is about 30.44 days, while the Moon's phase (synodic) cycle repeats on average every 29.53 days. Therefore the timing of the Moon's phases shifts by an average of almost one day for each successive month. 

(defn dozen? [x] 
  (= dozen x))

(dozen? (count '("jan" "feb" "mar" "apr" "may" "jun" "jul" "aug" "sept" "okt" "nov" "dec")))

;; Twelve is convenient because its multiples and divisors are convenient:

;; multiples 
(* 2 2 3)
(* 3 4)
(* 2 6)
(* 12 5)
(* 12 30)

;; divisors

;; 12 as numerator
(/ 12 2)
(/ 12 3)
(/ 12 4)

;; 12 as denominator
(/ 2 12)
(/ 3 12)
(/ 4 12)
(/ 360 12)

;; The use of twelve as a base number, known as the duodecimal system (also as dozenal), 
;; probably originated in Mesopotamia (see also sexagesimal)

;; This could come from counting on one's fingers by counting each finger bone with one's thumb. 
(def hand1 12)
(def hand2 12)

;; Using this method, one hand can count to twelve, and two hands can count to 144. 
;
;      
;               ____                           _.-._
;            .'`  __/_______           marker |1|4|7|_
;        ---'  -'`    1_2_3_)          thumb  |2|5|8|X|
;                     4_5_6_)            \    |3|6|9|X|  X for every multiple
;                    _7_8_9_)               _ |  '-._X|  of twelve so 3x12=36
;    jgs -----..___10_11_12)                \`\`-.'-._;
;                                            \    '   |
;                                             \  .`  /
;                                       power  |    |

;

;; Twelve *dozen* are known as a
(def gross 
  "the duodecimal equivalent of 100"
  (* 12 dozen))


;; Twelve *gross* are called a
(def great-gross 
  "a term most often used when shipping or buying items in bulk"
  (* 12 gross)) ;; the duodecimal version of 1,000
 
(def great-hundred
  "A great hundred, also known as a small gross, is 120 or ten dozen."
  (* 10 dozen))

 (def long-dozen
   "A baker's dozen is also known as a long dozen, thirteen."
   (inc dozen))





(comment "
  
      Reader Macros
      ---------------------------------
      '	    Quote: 'form → (quote form)
      \\	  Character literal (only one like)
      ;	    Single line comment
      ^	    Metadata (see Metadata section)
      @  	  Deref: @form → (deref form)
      `	    Syntax-quote
      ~	    Unquote
      ~@	  Unquote-splicing
      #"p"	Regex Pattern p
      #'	  Var quote: #'x → (var x)
      #()	  #(...) → (fn [args] (...))
      #_	  Ignore next form

")


;; same thing can be done in more ways
;; very readable:

(last (reverse [1 2 3]))   ;; last of reverse 1 2 3 ~ 3 2 1* ?
(-> [1 2 3] reverse last)  ;; pass 123 reverse 321 last 1

(-> "John" reverse join)





;; unquote splicing
(let [x `(2 3)] 
         `(1 ~x))

(let [x `(2 3)] 
         `(1 ~@x)) 


;; some sentences
(let [x '(bastard)
      y "he is"]
  
  (join `("John is " ~@x "but a good one " ~@y)))



(def portion (/ 5 12))
(rem 4 portion)


;; sequences et al

(list 1 2 3 4 5)
;; or shorthand
'(1 2 3 4 5 6 7)
;(quote (list 1 2 3 4 5))
;; map literal even amount
;{1 2 3 4 5 6}
;#{1 2 3 4 5 6 7}






;(def a (fn [] (+ 1 2 )))
;(source fn)
;`(fn ~ (+ 1 2 3))







;; concerning zeros ......................................

(zero? 0)
(zero? -1E-400)

(= 0 -0)
(== -0 0)

;; Probably .equals() method under the hood
(class -1E-400)

;; Which treats the positive and negative zeros as unequal. 
(= 0 -1E-400)

;; This behavior is a violation of the IEEE floating point standard. 

;; Correct behaviour as expected
(== 0 -1E-400)



