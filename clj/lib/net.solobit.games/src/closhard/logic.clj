(ns closhard.logic
  (:use clojure.core.unify))


  (def T '[?a ?b])

  (defn make-matcher
    [root]
    (fn [tmpl req]
      (-> req
          :url
          (.replace root "")
          (.split "/")
          seq
          (unify- tmpl))))

  (def match (make-matcher "http://foo.com/foo/"))

  (match T {:url "http://foo.com/foo/1/2"})
  ;;=> {?a 1, ?b 2}

  (match T {:url "http://foo.com/foo/1"})

  (defn make-genr
    [root]
    (fn [tmpl binds]
      (->> binds
           (subst tmpl)
           (interpose "/")
           (cons root)
           (apply str))))

  (def gen (make-genr "http://foo.com/foo/"))

  (gen T '{?a 1, ?b 2})
  ;;=> "http://foo.com/foo/1/2"



  (unify {:first '?first
          :last  '?last
          :genre :giallo}

         {:first "Dario"
          :last  "Argento"
          :genre :giallo})




  (unify '{:first ?nickname
           :last  ?last
           :genre :giallo}

         '{:first "Bar"
           :last  "Argento"
           :genre :giallo})



  (subst '[1 2 ?x ?y]
         '{?x [3 4 ?x 6]}) ;;<= missing ?y


  (subst '[1 2 ?x ?y ?z]
         '{?x [3 4 ?y 6]})


  (unifier '[(?a * ?x | 2) + (?b * ?x) + ?c]
           '[?z + (4 * 5) + 3])

  (unify '[(?a * ?x | 2) + (?b * ?x) + ?c]
         '[?z + (4 * 5) + 3])


  (unify '[(?a * ?x | 2) + (?b * ?x) + ?c]
         '[(?a * 5 | 2) + (4 * 5) + 3])

  ;;=> {?c 3, ?b 4, ?x 5}

  (unify '[(?a * 5 | 2) + (4 * 5) + 3]
         '[?z + (4 * 5) + 3])

  ;;=> {?z (?a * 5 | 2)}

  (= (subst '[?z + (4 * 5) + 3]
            '{?c 3, ?b 4, ?x 5
              ?z (?a * 5 | 2)})

     (subst '[(?a * ?x | 2) + (?b * ?x) + ?c]
            '{?c 3, ?b 4, ?x 5
              ?z (?a * 5 | 2)}))

  ;;=> true

  (subst '[(?a * ?x | 2) + (?b * ?x) + ?c]
         '{?c 3, ?b 4, ?x 5})

  (unify [1 2 3] '[?x & ?more])
  (unify [1 2 3] '[_ _ _ & ?more])
  (unify [:foo 1 2] '[?head & _])
  ;(unifier [1 2 3] '[?x & ?more])
