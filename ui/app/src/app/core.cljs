(ns app.core
  (:require [rum.core :as rum]))

(enable-console-print!)

(println "This text is printed from src/app/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce state (atom {:text "Hello world!"}))

(rum/defc app [state]
  [:div (:text @state)])

(defn render []
  (println "render")
  (rum/mount (app state)
             (.getElementById js/document "app")))

(defn on-js-reload []
  (render))
