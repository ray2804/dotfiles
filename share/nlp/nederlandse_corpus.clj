http://www.let.rug.nl/vannoord/bin/alpinods_dir?./cdb

(ns scandent.language.nederlands
  (:require [net.cgrand.enlive-html :as html]))

(def ^:dynamic *base-url* "http://www.fourlangwebprogram.com/fourlang/nl/ww_nl_A.htm")

(def ^:dynamic *url-sterke-werkwoorden* "http://www.dutchgrammar.com/nl/?n=Verbs.ir03")

;; dynamic fetch
(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))


;; selectors

;wikitext
(defn nl-sterk [] (map html/text (html/select (fetch-url *url-sterke-werkwoorden*) [:table.gray :td])))

(comment "TODO Achter de voltooid deelwoorden waarvoor we het hulpwerkwoord zijn gebruiken, staat steeds een asterisk (*).")

(defn print2
"Infinitief, verleden tijd enkelvoud,	verleden tijd meervoud,	voltooid deelwoord, vervoegingspatroon"
  [] (doseq [line
             (map (fn [[inf vte vtm vdw vp]]
                         (assoc {} :inf inf :vte vte :vtm vtm :vdw vdw :vp vp))
                             (partition 5 (nl-sterk)))]
        (println line)))

;(print2)
