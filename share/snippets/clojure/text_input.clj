(ns controls)


;; (defn text-input [name value & [{:keys [size class]}] ]
;; [:input {:name name :value value :size size :class class}])
;; (text-input "first_name" "John")
;; (text-input "first_name" "John" {:size 20})

(defn text-input
  "Takes a element name value and optionally some keys as named arguments.
  Rewritten as multiple-arity function."
  ;;internationalize Tower strings

  ; (nil? params -> generated)
  ([] (text-input (str "FormContact-Invoerveld") ; i18n + random = manual change
                  (str "") ; no initial values/defaults
                  {}))
  ([name value] (text-input name value {}))
  ([name value {:keys [size class]}] ; mix of normal and destructuring named parameters
  [:input {:name name :value value :size size :class class}]))
; returns vec of simple html-like structure
(text-input)
; simple single element forms produced by the function
(text-input "first_name" "John")
(text-input "last_name" "Smith" {:size 20 :class "foo"})

(defn add-range [in]
  (map #(text-input %1 %2)
       [(apply str (keys in))]
       [(apply str (vals in))]))

(add-range {"foo" "bar" "baz" "bara"})

(keys {"foo" "bar"})

