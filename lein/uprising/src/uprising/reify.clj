(ns uprising.reify)

;; ex 1 ;;

;;; This is a library for the shopping result.

(defrecord Banana [qty])
(defrecord Grape  [qty])
(defrecord Orange [qty])

;;; 'subtotal' differs from each fruit.

(defprotocol Fruit
  (subtotal [item]))

(extend-type Banana
  Fruit
  (subtotal [item]
    (* 158 (:qty item))))

(extend-type Grape
  Fruit
  (subtotal [item]
    (* 178 (:qty item))))

(extend-type Orange
  Fruit
  (subtotal [item]
    (* 98 (:qty item))))

;;; 'coupon' is the function returing a 'reify' of subtotal. This is
;;; when someone uses a coupon ticket, the price of some fruits is
;;; taken off 25%.

(defn coupon [item]
  (reify Fruit
    (subtotal [_]
      (int (* 0.75 (subtotal item))))))

;;; Example: To compute the total when someone bought 10 oranges,
;;;  15 bananas and 10 grapes using a coupon.
;;; foo=> (apply +  (map subtotal [(Orange. 10) (Banana. 15) (coupon (Grape. 10))]))
;;; 4685            ; (apply + '(980 2370 1335))
;;; foo=>


;; ex 2 ;;

;; Using a reified FileFilter implementation to obtain only directory files
(.listFiles (java.io.File. ".")
  (reify
    java.io.FileFilter
    (accept [this f]
      (.isDirectory f))))


;; ex 3 ;;

;;;; This example shows how to reify a multi-arity protocol function
;;;; (note the different style in defprotocol vs reify)

;; define a multi-arity protocol function blah
(defprotocol Foo
  (blah
    [this x]
    [this x y]))

;; define an anonymous extension via reify
(def r (reify Foo
         (blah [_ x] x)
         (blah [_ x y] y)))

;; invoke blah via the r instance
(blah r 1)   ;; => 1
(blah r 1 2)   ;; => 2
