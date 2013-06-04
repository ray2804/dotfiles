

(defn tt []
  (let [s ["⚀" "⚁" "⚂" "⚃" "⚄" "⚅"]
        r2 (sort (repeatedly 2 #(rand-nth s)))
        d1 (inc (.indexOf dice (first r2)))
        d2 (inc (.indexOf dice (last r2)))
        d (sort (list d1 d2))
        m (case d
            ((1 2)) '(1 1 2 2 5 5 6 6)
            ((2 2)) '(2 2 5 5)
            ((3 3)) '(3 3 4 4)
            ((4 4)) '(4 4 3 3)
            ((5 5)) '(5 5 2 2)
            ((6 6)) '(6 6 1 1)
            d)
        ]
  {:base d
   :symbols r2
   :modified m
   :moves (count m)
   :steps (apply + m)}))

(tt)
