
;; While Vars ensure safe use of mutable storage locations via thread isolation, transactional references (Refs) ensure safe shared use of mutable storage locations via a software transactional memory (STM) system. 

;; Clojure transactions should be easy to understand if you've ever used database transactions - they ensure that all actions on Refs are atomic, consistent, and isolated. 

;; All reads of Refs will see a consistent snapshot of the 'Ref world' as of the starting point of the transaction. The transaction will see any changes it has made. This is called the in-transaction-value.

;; All changes made to Refs during a transaction (via ref-set, alter or commute) will appear to occur at a single point in the 'Ref world' timeline 

;; No changes will have been made by any other transactions to any Refs that have been ref-set/altered/ensured by this transaction.
;; Changes may have been made by other transactions to any Refs that have been commuted by this transaction.

;; If a constraint on the validity of a value of a Ref that is being changed depends upon the simultaneous value of a Ref that is not being changed, that second Ref can be protected from modification by calling ensure.

;; Example
;; In this example a vector of references to vectors is created, each containing (initially sequential) unique numbers. Then a set of threads are started that repeatedly select two random positions in two random vectors and swap them, in a transaction. No special effort is made to prevent the inevitable conflicts other than the use of transactions.

;; Method with 4 arguments
(defn run [nvecs nitems nthreads niters]

  ;; Let binding form (local lexical scope)  
  (let [vec-refs (vec (map (comp ref vec)
                           (partition nitems (range (* nvecs nitems)))))
        
        swap #(let [v1 (rand-int nvecs)
                    v2 (rand-int nvecs)
                    i1 (rand-int nitems)
                    i2 (rand-int nitems)]

                ;; Function body
                (dosync
                 (let [temp (nth @(vec-refs v1) i1)]
                   (alter (vec-refs v1) assoc i1 (nth @(vec-refs v2) i2))
                   (alter (vec-refs v2) assoc i2 temp))))
        report #(do
                 (prn (map deref vec-refs))
                 (println "Distinct:"
                          (count (distinct (apply concat (map deref vec-refs))))))]
    (report)
    (dorun (apply pcalls (repeat nthreads #(dotimes [_ niters] (swap)))))
    (report)))

;(run 100 10 10 1000)
 