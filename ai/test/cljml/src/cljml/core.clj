(ns cljml.core
  (:use [clj-ml-dev io data filters classifiers]))

(def iris-arff "file:////home/baal/.ai/test/cljml/resources/iris.arff")

(clojure.repl/dir clj-ml-dev.classifiers)

(def ds (make-dataset "name" [:length :width {:kind [:good :bad]}] [ [12 34 :good] [24 53 :bad] ]))

ds

(dataset-seq ds)
(instance-to-map (first (dataset-seq ds)))
(instance-to-vector (dataset-at ds 0))


(def ds (load-instances :arff iris-arff))
(def discretize (make-filter :unsupervised-discretize {:dataset-format ds :attributes [:sepallength :petallength]}))

(def filtered-ds (filter-apply discretize ds))

(def filtered-ds (unsupervised-discretize ds {:attributes [:sepallength :petallength]}))

;(def filtered-ds (->> "file:////home/baal/.ai/test/cljml/resources/iris.arff")
;                           (load-instances :arff)
;                           (make-apply-filter :unsupervised-discretize {:attributes [0 2]}))
;
;(def classifier (make-classifier :decission-tree :c45))