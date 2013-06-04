
(ns backgammon)

(defn initial-board [] ;; rank
  [\- \- \- \- \- \-   ;; 5
   \- \- \- \- \- \-   ;; 4
   \- \- \X \- \- \-   ;; 3
   \- \- \- \- \- \-   ;; 2
   \- \- \- \- \- \-]) ;; 1
;; \a \b \c \d \e \f
;; file ---------->


(defn lookup [board pos]
  (let [
        [file rank] (map int pos)
        [fc rc]     (map int [\a \0])
        f           (- file fc)
        r           (* 6 (- 5 (- rank rc)))
        index       (+ f r)
       ]

    (board index)))

(lookup (initial-board) [\c \3])

(map int [\a \0])


