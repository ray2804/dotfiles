(ns uprising.unmangle
  (:use [clojure.contrib.str-utils])

(defn unmangle
  "Given the name of a class that implements a Clojure function,
  returns the function's name in Clojure.
  Note: If the true Clojure function name contains any underscores
  (a rare occurrence), the unmangled name will contain hyphens
  at those locations instead."
  {:added "1.0"
   :author "Stephen C. Gilardi"}
  [class-name]
  (.replace
    (re-sub #"^(.+)\$(.+)__\d+$" "$1/$2" class-name)
    \_ \-))

(defmacro current-function-name
  "Returns a string, the name of the current Clojure function."
  {:added "1.0"
   :author "Stephen C. Gilardi"}
  []
  `(-> (Throwable.) .getStackTrace first .getClassName unmangle))