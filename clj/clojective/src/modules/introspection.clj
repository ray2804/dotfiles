
(ns net.solobit.clojective
  (:use clojure.pprint clojure.repl)
  (:require [clojure.set]
            [clojure.reflect]))

(defn which-ns?
  "Return a string representation of the value set in special *earmuffs*
  variable *ns*. Usually these can be rebound."
  [] (str *ns*))
(which-ns?)




; All three do the same: find declared methods.
(.getDeclaredMethods (.getClass {:a 1}))
(-> {:a 1} .getClass .getDeclaredMethods pprint)
(-> clojure.lang.PersistentArrayMap .getDeclaredMethods pprint)



(dir clojure.reflect)


(apropos #"^ref")

(def a {})
(clojure.reflect/reflect a)

(find-doc #"^ref")