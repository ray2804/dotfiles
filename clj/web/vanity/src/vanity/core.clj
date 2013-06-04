(ns vanity.core
  (:require [garden.core :refer [css]]
            [garden.units :refer [px pt]]
            [garden.units :refer (px+ px* px- px-div)]
            [garden.stylesheet :refer [at-media]]))


(defn style []
  (ref nil


(dosync
 (alter game
        assoc-in ["game" :board "term3"]
        0))

(dosync
 (alter game
        update-in ["game" :board "term3"]
        inc))

(dosync
 (alter game
        assoc-in ["game" :players :p3]
        "Rob"))



;; ref
;; macro
;; encapsulate
;; pretty print

(letfn
(css [:body {:font-size "16px"}])
(css [:h1 :h2 {:font-weight "none"}])
(css [:h1 [:a {:text-decoration "none"}]])
(css [:h1 :h2 [:a {:text-decoration "none"}]])

(css [:h1 :h2 {:font-weight "normal"}
      [:strong :b {:font-weight "bold"}]])


(css [:a {:font-weight 'normal
          :text-decoration 'none}
      [:&:hover
       {:font-weight 'bold
        :text-decoration 'underline}]])

(css [:h1 {"font-weight" "normal"}])

(css [:h1 {:font-weight "normal"}])

(css [:h1 {'font-weight "normal"}])

(css [:h1 {30000 "nom-nom"}])

(css [:body {:font "16px sans-serif"}])

(css [:pre {:font-family "\"Liberation Mono\", Consolas, monospace"}])

(css [:.box
      {:-moz {:border-radius "3px"
              :box-sizing "border-box"}}])

(def reset-text-formatting
  {:font {:weight "normal" :style "normal" :variant "normal"}
   :text {:decoration "none"}})

(css [:a reset-text-formatting])

(defn partly-rounded
  ([r1] (partly-rounded r1 r1))
  ([r1 r2]
   {:border {:top-right-radius r1
             :bottom-left-radius r2}}))

(css [:.box (partly-rounded "3px")])

(css [:p {:font ["16px" "sans-serif"]}])

(css [:p {:font ["16px" '(Helvetica Arial sans-serif)]}])

(css (px 16))

(px 16)

(px (px 16))

(px (pt 1))

(px+ 1 2 3 4 6)

(px-div 2 4)

(px* 2 2)

(px* 2 (pt 1))

(css ^:screen [:h1 {:font-weight "bold"}])

(css ^{:min-width (px 768) :max-width (px 979)}
     [:container {:width (px 960)}])

(css [:a {:font-weight "normal"}
      [:&:hover {:color "red"}]
      ^:screen
      [:&:hover {:color "pink"}]])

(css
 (at-media {:min-width (px 768) :max-width (px 979)}
           [:.container {:width (px 960) :padding [0 (px 10)]}]
           [:.row {:width (px 940)}])
         (at-media {:max-width (px 480)}
           [:container {:width (px 480) :padding [0 (px 10)]}]
           [:.row {:width (px 460)}]))




