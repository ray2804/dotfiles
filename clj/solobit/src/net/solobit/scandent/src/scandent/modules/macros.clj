(ns net.solobit.utils)



;; ----------------------------------------------------------------------------
;; An anaphoric equivalent would have give you a syntax like:
;; (make foo 1 2 3 4 it 6 7 it 8 9)
(defmacro make [v & body]
   `(let [~'it ~v]
      (list ~@body)))

;; New syntax in Dutch
(defmacro plak [w & lichaam]
  `(let [~'vooraan ~w]
     (list ~@lichaam)))
;; ----------------------------------------------------------------------------



;; Fancy lambda signs
(defmacro λ [& sigs] `(fn ~@sigs))
(defmacro lambda [& sigs] `(fn ~@sigs))
(defmacro defλ [& sigs] `(defn ~@sigs))






;; ----------------------------------------------------------------------------

;; Metrics

(defmacro timerun
  "Output the time a operation/calculation has taken."
  [& body]
 `(time (dotimes [_# 1000] ~@body)))



;; Logic

(defmacro unless
  "Reverse of the (if a b c) conditional."
  [expr form]
    (list 'if expr nil form))
