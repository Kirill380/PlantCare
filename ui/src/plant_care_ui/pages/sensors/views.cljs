(ns plant-care-ui.pages.sensors.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.router.nav :as router]
            [cljs-react-material-ui.reagent :as ui]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.pages.sensors.events]
            [plant-care-ui.pages.sensors.subs]
            [plant-care-ui.components.app.views :refer [clipboard-button]]))

(def page-wrapper-style
  {:display "flex"
   :flex-direction "column"
   :margin-left 25
   :margin-right 25})

(defn sensors-page []
 [:div {:style {:display "flex"
                :flex-direction "column"
                :margin-left 25}}
   [:h2 "Sensors page"]
   [ui/raised-button {:type "button"
                      :label "Create new sensor"
                      :style {:width 256
                              :margin-bottom 25}
                      :on-click #(router/navigate! :sensor-by-id {:id "new"})}]])


(defn sensor-form [id]
  (let [new? (= id "new")
        init-state (if new?
                    (reagent/atom {})
                    (re-frame/subscribe [:sensor-by-id id]))
        form-state (reagent/atom @init-state)
        on-change-field (fn [field]
                          (fn [e]
                            (swap! form-state assoc field (-> e .-target .-value))))]
    (reagent/create-class
     {:component-did-update
      (fn []
        (when (and
                (nil? @form-state)
                (not (nil? @init-state)))
          (reset! form-state @init-state)))
      :reagent-render
        (fn [id]
          [:div {:style {:display "flex"}}
            [:form {:style {:display "flex"
                            :flex-direction "column"
                            :width 256}
                    :on-submit (fn [e]
                                 (.preventDefault e)
                                 (let [action (if new?
                                                :create-sensor/request
                                                :edit-sensor/request)]
                                   (re-frame/dispatch [action @form-state])))}
               [ui/text-field
                {:floating-label-text "Name"
                 :on-change (on-change-field :name)
                 :value (:name @form-state)}]
               [ui/text-field
                {:floating-label-text "Wifi Name"
                 :on-change (on-change-field :wifi-name)
                 :value (:wifi-name @form-state)}]
               [ui/text-field
                {:floating-label-text "Wifi Password"
                 :type "password"
                 :on-change (on-change-field :password)
                 :value (:password @form-state)}]
               [ui/text-field
                {:floating-label-text "Frequency"
                 :on-change (on-change-field :frequency)
                 :value (:frequency @form-state)}]
               [ui/raised-button {:type "submit"
                                  :label (if new?
                                           "CREATE"
                                           "SAVE")
                                  :primary true}]]])})))

(defn sensor-by-id-page [id]
  (let [firmware (utils/listen :last-created-firmware)]
    [:div {:style page-wrapper-style}
     [sensor-form id]
     [:div {:style {:width 256
                    :margin-top 15}}
       [clipboard-button "Copy Firmware Code" "#firmware" (nil? firmware)]]
     [ui/text-field
      {:id "firmware"
       :multi-line true
       :rows-max 15
       :floating-label-text "Firmware"
       :value (or firmware "")
       :on-change (fn [_] _)}]]))
