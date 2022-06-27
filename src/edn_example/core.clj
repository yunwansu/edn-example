(ns edn-example.core
  (:require [clojure.edn :as edn]))

(def sample-map {:foo "bar" :bar "foo"})

(defn convert-sample-map-to-edn
  "Converting a Map to EDN"
  []
  (prn-str sample-map))

(println "Let's convert a map to EDN: " (convert-sample-map-to-edn))

(println "Now let's convert the map back: " (edn/read-string (convert-sample-map-to-edn)))

(defrecord Goat [stuff things])

(def sample-goat (->Goat "I love Goats" "Goats are awesome"))

(defn convert-sample-goat-to-edn []
  (prn-str sample-goat))

(println "Let's convert our defrecord Goat into EDN: " (convert-sample-goat-to-edn))

; -> #edn_example.core.Goat{:stuff "...}
;prn-str provides us with a EDN tag for our defrecord out of the box: “#edn_example.core.Goat”.
;the EDN reader know that this is not a standard Clojure type


(def edn-readers {'edn_example.core.Goat map->Goat})

(defn convert-edn-to-goat
  "Convert EDN back into a Goat. We will use the :readers option to pass through a map of tags -> readers, so EDN knows how to handle our custom EDN tag"
  []
  (edn/read-string {:readers edn-readers} (convert-sample-goat-to-edn)))

(println "Let's try converting EDN back to a Goat: " (convert-edn-to-goat))


(defn alternative-edn-for-goat [^Goat goat]
  "Creates a different edn format for the goat"
  (str "#edn-example/Alt.Goat" (prn-str (mapcat identity goat))))

(println "Let's convert our Goat to our custom EDN format: " (alternative-edn-for-goat sample-goat))

(defn convert-alt-goat-edn
  "convert it into a Goat"
  [elems]
  (map->Goat (apply hash-map elems)))


(defn convert-alt-edn-to-goat []
  (edn/read-string {:readers {'edn-example/Alt.Goat convert-alt-goat-edn}} (alternative-edn-for-goat sample-goat)))

(println "Let's our custom EDN back into a Goat: " (convert-alt-edn-to-goat))


(defn default-reader
  "A default reader, for when we don't know what's coming in."
  [t v]
  {:tag t :value v})


(defn convert-unknown-edn
  "We don't know what this EDN is, so let's give it to the default reader"
  []
  (edn/read-string {:default default-reader} (alternative-edn-for-goat sample-goat)))

(println "Let's handle some unknown EDN: " (convert-unknown-edn))
