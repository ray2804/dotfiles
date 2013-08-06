(ns uprising.foo
  (:gen-class)
  (:require [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.exchange  :as le]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]))

(defn start-consumer
  "Starts a consumer bound to the given topic exchange in a separate thread"
  [ch topic-name plugin]

  (let [queue-name (format "framework.events.%s" plugin)

        ;; handler function taking a channel, destructuring header as meta data and taking a payload
        handler    (fn [ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
                     (println (format "[consumer] %s received %s" plugin (String. payload "UTF-8"))))]

    (lq/declare ch queue-name :exclusive false :auto-delete true)
    (lq/bind    ch queue-name topic-name)
    (lc/subscribe ch queue-name handler :auto-ack true)))

(defn -main
  [& args]
  (let [conn  (rmq/connect)
        ch    (lch/open conn)
        ex    "framework.events"
        plugins ["hiccup" "enlive" "garden"]]
    (le/declare ch ex "fanout" :durable false :auto-delete true)
    (doseq [p plugins]
      (start-consumer ch "framework.events" p))
    (lb/publish ch ex "" "PROJ FRMW EXT ADD {:name \"New Extension 1\"}" :content-type "text/plain" :type "luminus.update")
    (lb/publish ch ex "" "New plugin available: luminus-rabbitmq"  :content-type "text/plain" :type "luminus.update")
    (Thread/sleep 2000)
    (rmq/close ch)
    (rmq/close conn)))


;(-main)
;; (let [x "Hellowww"
;;       y (java.util.Date.)]
;;   (println {:x x :y y}))

;;langohr.core/*default-config*

