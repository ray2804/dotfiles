;;; ~/.lein/profiles.d/aiml.clj
;;;
;;; Leiningen profile for Artificial Intelligence and Machine Learning.
;;;
;;;

{:shared {:port 9229, :protocol "https"}
 :datakit [:shared {:servers ["qa.mycorp.com"]}]
 :console [:shared {:servers ["stage.mycorp.com"]}]
 :documentate [:shared {:servers ["prod1.mycorp.com", "prod1.mycorp.com"]}]}