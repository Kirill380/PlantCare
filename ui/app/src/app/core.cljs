(ns app.core
  (:require [rum.core :as rum]
            [app.router :refer [router route]]))

(enable-console-print!)

(defonce state (atom {:text "Hello world!"}))

(defn render []
  (rum/mount (router route state)
             (.getElementById js/document "app")))

(render)
