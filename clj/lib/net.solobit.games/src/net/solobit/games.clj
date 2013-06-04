(ns net.solobit.games)



(comment "
  The result of a die roll is determined by the way it is thrown,
  according to the laws of classical mechanics; they are made random
  by uncertainty due to factors like movements in the thrower's hand.
  ")


(comment "
  In Euclidean geometry, a Platonic solid is a regular, convex polyhedron.
  The faces are congruent, regular polygons, with the same number of faces
  meeting at each vertex. There are exactly five solids which meet those
  criteria; each is named according to its number of faces.")



(defn tuples
  [qty]
  (sort (repeatedly qty #(inc (rand-int 6)))))

(tuples 3)

(defn defroll [])
(defn defdice [])



(def dice {:pips true
           :symbols '(1 2 3 4 5 6)
           :faces (count (:pips roll))
           :edges
           })

(def roll {:tuples 2
           :sort true
           :dices '(\a \b)
           })

roll
