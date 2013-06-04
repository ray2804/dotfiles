
(ns trictrac)



(derive ::p1 ::player)
(derive ::p2 ::player)

(parents ::p1)
(isa? ::p1 ::player)


(defprotocol Welcome
  (greeting [g, name]))



(defrecord Player [name]
  Welcome
  (greeting [_, subject] (println "Hallo " subject ", ik ben Closhard " ".")))

(defrecord Tactics [tacid name description pro con drills])

(defrecord Strategy [stratid name description pro con planning momentum])

(defrecord Game [id started finished duration winner style players])




(Player. "rob")
