(ns uprising.refs)

;; Refs are/have
;; =============

;; * for "Coordinated Synchronous access to Many Identities";
;; * shared binding which can only be modified inside transactions;
;; * for state that needs to be synchronized between threads.

;; When to use
;; -----------

;; If you need to keep track of a bunch of different things and you will
;; sometimes need to do operations that write to several of the things
;; at once, use refs.

;; Any time you have multiple different pieces of state, using refs
;; isn't a bad idea.

;; Variables
;; ---------

;; Vars are for `thread local isolated identities` with a shared default value.

;; Vars are for when you need to store something on a per-thread basis.
;; If you have a multi-threaded program and each thread needs its own
;; private state, put that state in a var.

;; Vars are created through `def` (and thus `defn`) definitions.

;; Define a Var using a Ref data type with 0 as value
;; to use as integer counter (incremental updates of 1).

(def my_num (ref 0))


;; Value of a ref can be set in 3 ways: ref-set, commute and alter.

(defn increment
  "Transactions are wrapped in `dosync` and behave similarly to database transactions.
  Value of a ref can be set in 3 ways: ref-set, commute and alter. This function uses alter."
  [form]
  (dosync
    (try
      ;; Explanation: when alter detects that the ref has been changed in
      ;; another concurrent transaction, the whole transaction is rolled back.

      ;; With commute, no transaction is ever rolled back. But it also means that inside the
      ;; transaction a stale value can be used, what is the reason for getting “45″ several times.
      (println "Operation: " (str form) (form my_num inc))

      ;; Error trapping
      (catch Throwable t
        (do
          (println "Caught " (.getClass t))
          (throw t))))))

(defn test_alter
  "Run incremental update of 1 n times."
  [n form]
  ;; Create a new thread for each number n
  (let [threads (for [x (range 0 n)]
                  (Thread. #(increment form)))]
    (do
      (println (str "Alter " n " times" ))
      (doall (map #(.start %) threads))
      (doall (map #(.join %) threads))
      (println "---- After loop: ----")
      (increment form))))

(test_alter 50 commute)

;; Note I changed `alter` to [form] parameter so we may also use `commute` in its place.