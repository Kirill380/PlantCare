(ns plant-care-ui.pages.connections.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame-datatable.core :as dt]
            [plant-care-ui.router.nav :as router]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.pages.connections.events]
            [plant-care-ui.pages.connections.subs]))
            ; [plant-care-ui.components.app.views :refer [plant-card]]))

(def page-wrapper-style
 {:display "flex"
  :flex-direction "column"
  :margin-left 25
  :margin-right 25})

(defn sensor->list-item [sensor]
  [ui/menu-item {:value (:id sensor)
                 :key (:id sensor)
                 :primary-text (:name sensor)}])

(defn add-connection-form []
  (let [selected-sensor (reagent/atom nil)
        selected-plant (reagent/atom nil)]
    (fn []
      (let [sensors (utils/listen :all-sensors-list)
            sensor-items (map sensor->list-item sensors)
            plants (utils/listen :all-flowers-list)
            plant-items (map sensor->list-item plants)]
        [:div {:style {:display "flex"
                       :align-items "center"}}
          [ui/select-field
            {:floating-label-text "Select sensor"
             :on-change (fn [_ _ v]
                          (println v)
                          (reset! selected-sensor v))
             :value @selected-sensor}
            sensor-items]
          [ui/select-field
            {:floating-label-text "Select plant"
             :on-change (fn [_ _ v]
                          (reset! selected-plant v))
             :value @selected-plant}
            plant-items]
          [ui/raised-button
            {:label "Create connection"
             :disabled (or
                         (nil? @selected-sensor)
                         (nil? @selected-plant))
             :primary true
             :on-click #(re-frame/dispatch
                          [:connect-sensor-to-plant/request
                            @selected-sensor
                            @selected-plant])}]]))))

(defn connections-page []
  (let [open? (reagent/atom true)]
    (fn []
      [:div {:style page-wrapper-style}
        [:h2 "Connections page"]
        [ui/raised-button
          {:label "Add connection"
           :style {:width 256}
           :on-click #(swap! open? not)}]
        (when @open? [add-connection-form])
        [:button
          {:on-click #(re-frame/dispatch [:get-plants-for-present-sensors])}
          "click me"]])))
