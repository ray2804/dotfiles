;;; Mutation Facilities

;;; Employee Record Manipulation

;;; Data structures and sequences in Clojure are immutable as seen in the examples presented in Clojure_Programming/Concepts#Structures 
;;; Managing state in a language with immutable sequences and data structures is a frequent source of confusion for people used to programming 
;;; languages that allow mutation of data.

;;; Use set/fn-name rather than clojure.set/fn-name
(alias 'set 'clojure.set)

(defstruct employee
           :name :id :role) ; == (def employee (create-struct :name :id ..))
 
(def employee-records (ref #{}))
 
;;;===================================
;;; Private Functions: No Side-effects
;;;===================================
 
(defn- update-role [n r recs]
  (let [rec    (set/select #(= (:name %) n) recs)
        others (set/select #(not (= (:name %) n)) recs)]
    (set/union (map #(set [(assoc % :role r)]) rec) others)))
 
(defn- delete-by-name [n recs]
  (set/select #(not (= (:name %) n)) recs))
 
;;;=============================================
;;; Public Function: Update Ref employee-records
;;;=============================================
(defn update-employee-role [n r]
  "update the role for employee named n to the new role r"
  (dosync 
    (ref-set employee-records (update-role n r @employee-records))))
 
(defn delete-employee-by-name [n]
  "delete employee with name n"
  (dosync
    (ref-set employee-records
             (delete-by-name n @employee-records))))
 
(defn add-employee [e]
  "add new employee e to employee-records"
  (dosync (commute employee-records conj e)))
 
;;;=========================
;;; initialize employee data
;;;=========================
(add-employee (struct employee "Jack" 0 :Engineer))
(add-employee (struct employee "Jill" 1 :Finance))
(add-employee (struct-map employee :name "Hill" :id 2 :role :Stand))