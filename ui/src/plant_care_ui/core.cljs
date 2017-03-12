(ns plant-care-ui.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.router.core :refer [router]]
            [plant-care-ui.db.events]))

(enable-console-print!)

(defonce state (atom {:text "Hello world!"}))

(defn render []
  (reagent/render [router]
   (.getElementById js/document "app")))

(re-frame/dispatch-sync [:init-db])
(render)
