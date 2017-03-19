(ns plant-care-ui.components.app.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.components.material :as m]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.components.app.subs]
            [plant-care-ui.components.app.events]))


(def toggle-drawer #(re-frame/dispatch [:app/toggle-drawer]))

(defn app [child]
  (let [open? (utils/listen :app/drawer-open?)]
   [:div
    [m/app-bar {:title "Title"
                :on-left-icon-button-touch-tap toggle-drawer}]
    [m/drawer {:docked false
               :open open?
               :on-request-change toggle-drawer}]
    child]))
