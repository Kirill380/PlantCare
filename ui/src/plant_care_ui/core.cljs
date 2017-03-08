(ns plant-care-ui.core
  (:require [reagent.core :as reagent]
            [plant-care-ui.router.core :refer [router]]))

(enable-console-print!)

(defonce state (atom {:text "Hello world!"}))

(defn render []
 (reagent/render [router]
  (.getElementById js/document "app")))

(render)
