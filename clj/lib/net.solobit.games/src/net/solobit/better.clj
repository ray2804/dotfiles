
(defn weighted
  "Given a map of generators and weights, return a value from one of
  the generators, selecting generator based on weights."
  [m]
  (let [weights   (reductions + (vals m))
        total   (last weights)
        choices (map vector (keys m) weights)]
    (let [choice (uniform 0 total)]
      (loop [[[c w] & more] choices]
        (when w
          (if (< choice w)
            (call-through c)
            (recur more)))))))

(defn tt []
  "Simple trictrac game with dice values 1..6"
  (let [sy ["⚀" "⚁" "⚂" "⚃" "⚄" "⚅"]
        r2 (sort (repeatedly 2 #(rand-nth sy)))
        d1 (inc (.indexOf dice (first r2)))
        d2 (inc (.indexOf dice (last r2)))
        dd (sort (list d1 d2))
        m (case dd
            ((1 2)) '(1 1 2 2 5 5 6 6)
            ((2 2)) '(2 2 5 5)
            ((3 3)) '(3 3 4 4)
            ((4 4)) '(4 4 3 3)
            ((5 5)) '(5 5 2 2)
            ((6 6)) '(6 6 1 1)
            dd)
        c (count m)
        s (apply + m)
        rs (map #(.indexOf sy %) m)

        ]
  {:base dd
   :symbols r2
   :result m

   :moves c
   :steps s}))


(:symbols (tt))




(def my-list (list "⚀" "⚁" "⚂" "⚃" "⚄" "⚅"))
(def index-map (zipmap my-list (range)))