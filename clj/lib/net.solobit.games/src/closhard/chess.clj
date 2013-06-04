(ns games.chess)

(def *file-key* \a)
(def *rank-key* \0)

(defn initial-board []
  [\r \n \b \q \k \b \n \r
   \p \p \p \p \p \p \p \p
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \P \P \P \P \P \P \P \P
   \R \N \B \Q \K \B \N \R])

;; black lowercase, white UPPERCASE
;; suboptimal but good start


(defn- file-component [file]
  (- (int file) (int *file-key*)))

(defn- rank-component [rank]
  (* 8 (- 8 (- (int rank) (int *rank-key*)))))

(defn- index [file rank]
  (+ (file-component file) (rank-component rank)))


(letfn [(index [file rank]
               (let [f (- (int file) (int \a))
                     r (* 8 (- 8 (- (int rank) (int \0))))]
                 (+ f r)))]
  ;; define function inside the local scope
  (defn lookup [board pos]
    (let [[file rank] pos]
      (board (index file rank)))))


(defn lookup2 [board pos]
  (let [
        [file rank] (map int pos)
        [fc rc]     (map int [\a \0])
        f           (- file fc)
        r           (* 8 (- 8 (- rank rc)))
        index       (+ f r)
       ]

    (board index)))

(lookup2 (initial-board) [\c \3])

(let [[a b] (map int [1 1 2])]
  a)

(int \b)

