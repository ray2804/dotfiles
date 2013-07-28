(ns foo
  (:use markdown.core))

(def a (slurp "/home/baal/.lein/profiles.clj"))

(md-to-html-string "# This is a test\nsome code follows\n```clojure\n(defn foo [])\n```")

(vals (ns-refers *ns*))

*read-eval*

(defn which-ns? []
  (let [t (type *ns*)
        v (str *ns*)
        r *ns*]
    (str "
         ")))
(which-ns?)

(str "foo
     ")

*out*

*in*
