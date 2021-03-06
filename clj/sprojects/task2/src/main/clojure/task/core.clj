(ns task2.core
  (:require [clj-time.core :as time])
  (:require [mikera.cljutils.text :as text])
  (:require [clojure.pprint])
  (:require [clojure.repl]))

;; ====================================================================================
;; Task data structure

(defrecord TaskData [])

(defn task*
  "Creates a task data structure representing the specified function as a task"
  ([options function]
	  (TaskData. nil
	    (merge options
	           {:function function
	            :promise (promise)
	            :creation-time (time/now)}))))


;; =======================================================================================
;; Task status

(defonce last-task-counter (atom 0))

(defonce tasks (ref {}))

(defn allocate-task-id []
  (swap! last-task-counter inc))

;; =====================================================================================
;; Task query functions

(defn get-task
  "Returns the current task associated with the given ID.

  If called with an old task instance, returns the latest task for the same ID"
  [task-or-id]
  (let [id (cond
						 (associative? task-or-id) (:id task-or-id)
						 :else task-or-id)
        task (@tasks id)]
    task))

(defn stopped? [task]
  (let [task (get-task task)]
    (or
      (nil? task)
      (#{:stopped} (:status task)))))


(defn complete? [task]
  (let [task (get-task task)]
    (or
      (nil? task)
      (#{:complete :error :stopped} (:status task)))))

(defn running? [task]
  (let [task (get-task task)]
    (and task
       (#{:started} (:status task)))))


(def task-fields
  {:id {:length 6}
   :status {:length 10}
   :source {:length 20}
   :options {:length 20}
   :result {:length 15}})

(def task-summary-fields [:id :status :source :options :result])

(defn task-summary [task & {:keys [fields]
                            :or {fields task-summary-fields}}]
  (reduce
    (fn [m k]
      (assoc m k
             (let [len (:length (task-fields k))
                   s (str (k task))]
               (text/pad-right (text/truncate-dotted s len) len))))
    {}
    fields))


;; =====================================================================================
;; Task creation

(defmacro wrap-if [condition form]
  `(if ~condition
     (fn [~'code] ~form)
     identity))

(defn build-task-function
  "Create the source code for the function to be called for each task execution.
   Code generation is done based on the provided options - you only pay for the
   options selected."

  ([{repeat :repeat
     sleep-millis :sleep
     accumulate :accumulate-results
     repeat-value :repeat
     while-clause :while
     until-clause :until
     :as options} original-code]
	  (-> `(assoc ~'task :result ~original-code)
	    ((wrap-if accumulate
	              `(let [task# ~code]
	                 (assoc task# :results (conj (or (:results task#) []) (:result task#))))))
	    ((wrap-if until-clause
	              `(let [~'task ~code]  ;; need to let task so that until-clause sees this value
                   (if ~until-clause
	                   (assoc ~'task :status :complete)
                     ~'task))))
	    ((wrap-if while-clause
	              `(if ~while-clause
	                 ~code
	                 (assoc ~'task :status :complete))))
 	    ((wrap-if (number? repeat-value)
		            `(let [task# ~code
		                   rep# (dec (:repeat task#))]
		               (if (> rep# 0)
		                 (assoc task# :repeat rep#)
		                 (assoc task# :status :complete :repeat 0)))))
 	    ((wrap-if (not repeat-value)
					      `(let [task# ~code]
					         (assoc task# :status :complete))))
	    ((wrap-if sleep-millis
	              `(let [task# ~code]
	                 (if (not (complete? ~'task)) (do (Thread/sleep ~sleep-millis) task#))
	                 task#)))
	    ((wrap-if true
	              `(fn [~'task]
                   ~code)))))

  ([code] (build-task-function nil code)))

(defmacro task
  "Creates a task containing the given code. Valid options are:
  :repeat
     (Default) If left nil of false, the task will only execute once
     If a numeric value is used, the task will repeat the given number of times.
     If any other true value is used, the task will repeat infinitely.

  :result
     An initial result value for the task. :result will be set to the value produced
     by the task on each iteration.

  :accumulate-results
     If set to true, all results will be saved in the vector :results in the task.

  :timeout
     A number of milliseconds to run the task for. If the timeout is reached during
     execution of the task, it will be allowed to complete.

  :sleep
     A number of milliseconds to sleep between successive executions of the task

  :while
     Code that will be called before each iteration with the task as an argument.
     If it returns true, then the task will complete.

  :until
     Code that will be evaluated after each iteration with the task as an argument.
     If it returns true then the task will complete.
     "

  ([options code]
    (let [function (build-task-function options code)
          options (merge options
                         {:source (str code)
                          :options options})]

      `(task* (quote ~options) ~function)))
  ([code]
    `(task nil ~code)))



 (defn print-table2 [aseq column-width]
      (binding [*out* (clojure.pprint/get-pretty-writer *out*)]
        (doseq [row aseq]
          (doseq [col row]
            (clojure.pprint/cl-format true "~4D~7,vT" col column-width))
          (prn))))


;; =====================================================================================
;; Task management functions


(defn ps
  "Prints a table of tasks"
  ([& {:keys [filter fields tasks sort]
       :or {fields task-summary-fields
            filter (constantly true)
            sort :id
            tasks (vals @tasks)}}]
    (let [tasks (clojure.core/filter filter tasks)
          tasks (if sort (sort-by sort tasks) tasks)]
      (clojure.pprint/print-table fields (map task-summary tasks)))))
      ;(print-table2 fields (map task-summary tasks)))))

(defn stop [task]
    (dosync
		  (let [task (get-task task)
		        id (:id task)]
		      (alter tasks assoc id (assoc task :status :stopped)))))

(defn stop-all
  ([]
    (doseq [[id task] @tasks]
      (stop task))))

(defn await-result [task]
  (let [task (get-task task)]
    @(:promise task)))

(defn result [task]
  (let [task (get-task task)]
    (:result task)))

(defn await-task [task]
  (let [task (get-task task)]
    @(:promise task)
    (get-task task)))

(defn await-results [task]
  (let [task (get-task task)]
    @(:promise task)
    (:results (get-task task))))

(defn clear
  "Clears all currently runnig tasks"
  ([]
    (dosync
      (ref-set tasks {})))
  ([task]
	  (let [task (get-task task)
	        id (:id task)]
	    (dosync
	      (alter tasks dissoc id)))))

;; ==================================================================================
;; Task execution

(defn- finish-task
  ([task]
    (let [task (get-task task)
          task (assoc task :finish-time (time/now))
          promise (:promise task)]
      (if promise (deliver promise (:result task)))
      task)))

(defn task-loop [task]
  (if (complete? task)
	  (finish-task task)
    (let [id (:id task)
          task (try
                 ((:function task) task)
                 (catch Throwable t
                   (clojure.repl/pst t)
                   (assoc task :status :error :error t :result t)))
          new-status (if (stopped? (get-task id)) :stopped (:status task))
          task (assoc task :status new-status)]
	    (dosync (alter tasks assoc id task))
      (recur (@tasks id)))))

(defn elapsed-time-secs [task]
  (if-let [task (get-task task)]
    (let [start-time (:start-time task)
          time-now (time/now)]
      (* 0.001 (.toDurationMillis (time/interval start-time (time/now)))))))

(defn run-task [task]
  (let [id (allocate-task-id)
        task (merge task
								    {:id id
                     :status :started
								     :start-time (time/now)})]
	  (dosync (alter tasks assoc id task))
    (future (task-loop task))
    task))

;; =========================================================================================
;; Task launching macros

(defmacro run
  "Create and run a new task.

  Calls task with the relevant options."
  ([code]
	 `(run-task (task ~code)))

  ([options code]
	 `(run-task (task ~options ~code)))

  ([code k1 v1 & more]
	 `(run-task (task {~k1 ~@(cons v1 more)} ~code))))


