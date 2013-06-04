(ns tredius.models.schema
  (:require [clojure.java.jdbc :as sql]
            [noir.io :as io]))

(def db-store "site.db")

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname (str (io/resource-path) db-store)
              :user "sa"
              :password ""
              :naming {:keys clojure.string/lower-case
                       :fields clojure.string/upper-case}})
(defn initialized?
  "checks to see if the database schema is present"
  []
  (.exists (new java.io.File (str (io/resource-path) db-store ".h2.db"))))




(defn create-expenses-table
  []
  (sql/with-connection db-spec
    (sql/create-table
      :survey_expenses
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:postcode "varchar(30)"]
      [:household "tinyint"]
      [:income "decimal(20,2)"]
      [:home_owner :boolean]
      [:submitted "timestamp"])
    (sql/do-commands
      "CREATE INDEX timestamp_index ON survey_expenses (timestamp)")))



(defn create-users-table
  []
  (sql/with-connection db-spec
    (sql/create-table
      :users
      [:id "varchar(20) PRIMARY KEY"]
      [:first_name "varchar(30)"]
      [:last_name "varchar(30)"]
      [:email "varchar(30)"]
      [:admin :boolean]
      [:last_login :time]
      [:is_active :boolean]
      [:pass "varchar(100)"])))


(defn create-tables
  "creates the database tables used by the application"
  []
  (create-expenses-table))
