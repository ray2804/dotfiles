(ns clj1374
  (:require [dire.core :refer [with-handler!]]))






;;; Define a task to run. It's just a function.
(defn divider [a b]
  (/ a b))

;;; For a task, specify an exception that can be raised and a function to deal with it.
(with-handler! #'divider
  "Here's an optional docstring about the handler."
  java.lang.ArithmeticException
  ;;; 'e' is the exception object, 'args' are the original arguments to the task.
  (fn [e & args] (println "Cannot divide by 0.")))

(divider 10 0) ; => "Cannot divide by 0."
(def DEBUG false)
(defmacro on-debug [& body]
  `(when DEBUG
     (do ~@body)))

(on-debug (println "FOO"))

(defn with-handlers! [f coll]
  )

(with-handlers! #'divider
  [java.lang.ArithmeticException (fn [e & args] (println "Cannot divide by 0."))])