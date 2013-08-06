(ns
  ^{:original-work "Caleb Peterson 20xx (c)"
    :current-maintainer "Rob Jentzema 2013 (c)"

    :doc "

    ClipboardTransferable
    ---------------------

    Reads all of the data from the system Clipboard which the data transfer
    subsystem knows how to translate. This includes all text data, File Lists,
    Serializable objects, Remote objects, and properly registered, arbitrary
    data as InputStreams. The data is stored in byte format until requested
    by client code. At that point, the data is converted, if necessary, into
    the proper format to deliver to the application.

    This hybrid pre-fetch/delayed-rendering approach allows us to circumvent
    the API restriction that client code cannot lock the Clipboard to discover
    its formats before requesting data in a particular format, while avoiding
    the overhead of fully rendering all data ahead of time.

    "}

  uprising.snipsnap

  (:import (java.io File FileInputStream)
           (javax.swing JFileChooser)
           (javax.imageio ImageIO)
           (java.awt.image BufferedImage DataBuffer DataBufferByte)
           (java.awt Toolkit Image Event)
           (java.awt.datatransfer Clipboard
                                  ClipboardOwner
                                  Transferable
                                  StringSelection
                                  DataFlavor
                                  UnsupportedFlavorException
                                  FlavorListener))

  (:require [clojure.data.codec.base64 :as b64]
            [clojure.contrib.str-utils :as str-utils]
            [clojure.java.io :as io]
            [clojure.java.io :refer [as-file]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Object retrieval methods

(defn get-clipboard
  "Returns the AWT realized system clipboard object."
  []
  ;; Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
  (-> (Toolkit/getDefaultToolkit)
      (.getSystemClipboard)))

(defn get-content
  "Invokes the void `getContents` method of the clipboard returning a
  object derived from a class implementing the Transferable interface."
  []
  ;; Transferable java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
  (.getContents (get-clipboard) nil))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Predicates

(defn image?
  "Returns true if the current content of the clipboard is of
  DataFlavor imageFlavor type and can be transfered as such."
  []
  (.isDataFlavorSupported (get-content)
                          DataFlavor/imageFlavor))

(defn text?
  "Returns true if the current content of the clipboard is of
  DataFlavor stringFlavor type and can be processed as such."
  []
  (.isDataFlavorSupported (get-content)
                          DataFlavor/stringFlavor))

(defn file?
  "Returns true if the form is of the type java.io.File"
  [in]
  (if (= (type in) java.io.File)
    true false))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Clipboard functions



(defn get-text
  "Get text from content if predicate evaluates to true, the correct DataFlavor
  transfer method is invoked on the content."
  []
  (if-let [content (if (text?) (get-content))]
    (.getTransferData content DataFlavor/stringFlavor)))

(defn get-image
  "Get BufferedImage from content if predicate evaluates to true, the correct DataFlavor
  transfer method is invoked on the content."
  []
  (if-let [content (if (image?) (get-content))]
    (.getTransferData content DataFlavor/imageFlavor)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Image utilities
;; Much thanks to Joseph Wilk for inspiration
;; https://github.com/josephwilk/image-resizer

(def image-folder "resources/out")

(defn buffered-image
  "Returns a BufferedImage instance or, if not provided creates one by reading the file."
  [image]
  (if (instance? BufferedImage image)
    image
    (if (file? image)
      (ImageIO/read image))))

(defn write-image!
  "Persists a image, from the current clipboard, to disk.
  Invokes the buffered-image function"
  [filename & {:keys [ext path]}]
  (io!
   (ImageIO/write (buffered-image (get-image)) "png"
                  (File. (str image-folder "/" filename)))))

(defn dimensions
  "Returns dimensions (height, width) of any image given as input."
  [image]
  [(.getWidth image) (.getHeight image)])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Text utilities

(defn text-replace
  "Replaces any occurance of regular expression and replaces it
  with substitute anywhere it is found in the input string. May
  be used to moderate any clipboard texts and replace tokens."
  [re sub string]
  (str-utils/re-gsub re sub string))

(defn text-word-count
  "Defines a function to build a word histogram, takes a collection of words
  and builds a hash-map where each key is a unique word, and each value is the
  count of how many times that word appears in the collection."
  [words]
  (reduce
    #(assoc %1 %2 (inc (get %1 %2 0)))
    {}
    words))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(def content (atom (get-content)))

(def
  ^{:doc "Extends thread and implements ClipboardOwner"}
  owner
  (reify ClipboardOwner
    (lostOwnership [this clipboard contents] ())))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Watchers / listeners

(defn watch [key f]
  (add-watch content key f))

(defn unwatch [key f]
  (remove-watch content key))

;; Listen to the clipboard for content changes, executed as file is loaded.
(let [listener (reify FlavorListener (flavorsChanged [this e]
                                                     (reset! content (get-content))))]
  (.addFlavorListener (get-clipboard) listener))

(load "foo")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Side-effects

(defn set-content!
  "Performs side-effects, changes clipboard content to contain new value(s)."
  [^Transferable content]
  (let [current (get-content)]
    (.setContents (get-clipboard) content owner)
    current))

(defn set-text!
  "Function with pure side-effects, set text for content."
  [^String text]
  (let [current (get-text)]
    (set-content! (StringSelection. text))
    current))



(defn make-thumbnail
  [filename new-filename width]
  (let [img (javax.imageio.ImageIO/read (as-file filename))
        imgtype (java.awt.image.BufferedImage/TYPE_INT_ARGB)
        width (min (.getWidth img) width)
        height (* (/ width (.getWidth img)) (.getHeight img))
        simg (java.awt.image.BufferedImage. width height imgtype)
        g (.createGraphics simg)]
    (.drawImage g img 0 0 width height nil)
    (.dispose g)
    (javax.imageio.ImageIO/write simg "png" (as-file new-filename))))

;; (-> (get-content)
;;     (.getTransferDataFlavors))

;; evil! ;(eval (read-string (get-text))) ;