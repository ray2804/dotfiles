
(ns game.movement)

(def ascii {:slot "| |"
            :left "<<"
            
            })

(ns game.symbols)

(def data ["| |" "." "o" "x" "/ \\"])
(def alphabet '(\A \B \C \D \E \F \G \H \I \J \K \L \M 
                \N \O \P \Q \R \S \T \U \V \W \X \Y \Z))


(def left "<<")

(ns game.boards
  (:require [game.movement :as move]
            [clojure.zip :refer [zipper down right left up]]
            [clojure.pprint :refer [print-table pprint]]))

(def board [
            (repeat 4 (repeat 6 [0]))
     
            ])

(def z [[:a :b \. [\A] \. \. \. \.]
        [1 2 3 4 5 6] 
        [4 [5 6] 7] 
        [8 9]])
(def zp (zipper vector? seq (fn [_ c] c) z))

(-> zp down down)
(clojure.repl/doc print-table)

(ns game.settings
  (:require [game.symbols :as sign]))


(println ::left)

(def game-state 
  "Persistent state"
  (atom {:game {:saved "/usr/games/saved/tt130102.dat"}
         :p1 {:stack (first sign/alphabet) :name "Player 1"}
         :p2 {:stack (first (reverse sign/alphabet)) :name "Player 2"}
                 
         }))
(:game @game-state)

(defn setCell
  [row col value]
  (clojure.pprint/pprint value)
 )

(map #(setCell 0 %1 %2) (iterate inc 0) data)

(map-indexed vector (range 0 24))
