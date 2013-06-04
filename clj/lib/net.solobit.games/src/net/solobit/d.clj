(ns net.solobit.games)

(comment "
  Each Platonic solid can therefore be denoted by a symbol {p, q} where
  p = the number of edges of each face (or the number of vertices of each face) and
  q = the number of faces meeting at each vertex (or the number of edges meeting at each vertex).

  The symbol {p, q}, called the Schläfli symbol, gives a combinatorial description of the polyhedron.

  All other combinatorial information about these solids, such as total number of
  vertices (V), edges (E), and faces (F), can be determined from p and q.

  Two relationships:
  pF = 2E = qV
  Euler: V - E + F =2

  ")



(defn calc-shape [p q]
  (let [m (- 4 (- p 2) (- q 2))
        v (/ (* 4 p) m)
        e (/ (* 2 p q) m)
        f (/ (* 4 q) m)]
  (str v " " e " " f)))







(def polyhedron
  {"Platonic solid"
   {:name "tetrahedron"
    :vertices 4
    :edges 6
    :faces 4
    :p 3
    :q 3
    :proof (= 2 (+ 4 (- 4 6)))
    :symbol '(3 3)
    :vconf '(3 3 3)}

   {:name "hexahedron / cube"
    :vertices 8
    :edges 12
    :faces 6
    :symbol '(4 3)
    :vconf '(4 4 4)}

   {:name "octahedron"
    :vertices 6
    :edges 12
    :faces 8
    :symbol '(3 4)
    :vconf '(3 3 3 3)}

   {:name "dodecahedron"
    :vertices 20
    :edges 30
    :faces 12
    :symbol '(5 3)
    :vconf '(5 5 5)}

	 {:name "icosahedron"
    :vertices 12
    :edges 30
    :faces 20
    :symbol '(3 5)
    :vconf '(3 3 3 3 3)}
   })


polyhedron
