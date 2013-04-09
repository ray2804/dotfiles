;;; ~/.lein/profiles.clj
;;;
;;; Leiningen global base :user profile for projects (common libraries)
;;;
;;; READ ME! This contains important information on the mechanics of
;;; Leiningen 2 global and local profiles and project plugins and
;;; comfortable dependency management.
;;;
;;; The `:default` profile is defined to be a composite of
;;; `[:dev :provided :user :base]` (changable in project.clj).

;; Local project and profiles files (:dev)
;; --------------------------------

;; In addition to the local `project.clj`, profiles also can be specified in 
;; `profiles.clj` within the project root. Profiles in =project= `profiles.clj` 
;; will override those in `project.clj`.

;; The `:dev` profile should go here. Never define a `:user` profile here!

;; Global profiles (~/.lein/..) (:users)
;; ----------------------------

;; Global profiles will be available in all projects managed by Leiningen, though
;; those profiles will be **overridden** by profiles of the same name specified 
;; in the project. Defining the same global profile in multiple different files
;; is considered an error.

;; The `:user` profile is separate from `:dev`; the latter is intended to be
;; specified in the project itself. In order to avoid collisions, the project
;; should never define a `:user` profile, nor should a global `:dev` profile be
;; defined. Use the `show-profiles` task to see what's available.

{:user {:plugins [
  
         ;; Source control
         [lein-gitify "0.1.0"]      ; Create and initializing a Github repository
         
         ;; Project management
         [lein-pprint "1.1.1"]      ; Pretty-print representation of project map
         [slamhound "1.3.1"]        ; Rips your namespaces apart and reconstructs it
         
         ;; Bootstrapping utilities
         [lein-cljsbuild "0.1.8"]   ; Makes ClojureScript development easy
         [lein-libdir "0.1.1"]      ; Copy jar deps into the project lib directory
         [lein-pprint "1.1.1"]      ; Pretty-print a representation of the project map
         
         ;; Development environments and text editors
         [lein-catnip "0.5.1"]      ; Web based IDE text editor and REPL
         [lein-light "0.0.24"]      ; LightTable plugin to connect to the IDE
         
                 ]

  :search-page-size 60
  :repl-options {:prompt #([ns] (str "My liege? "))}
}}

