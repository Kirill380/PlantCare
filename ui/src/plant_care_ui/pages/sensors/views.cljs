(ns plant-care-ui.pages.sensors.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.router.nav :as router]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame-datatable.core :as dt]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.pages.sensors.events]
            [plant-care-ui.pages.sensors.subs]
            [plant-care-ui.components.app.views :refer [clipboard-button]]))

(def page-wrapper-style
  {:display "flex"
   :flex-direction "column"
   :margin-left 25
   :margin-right 25})

(defn sensors-table []
  [dt/datatable
   :tables/all-sensors
   [:all-sensors-list]
   [{::dt/column-key [:id]
     ::dt/column-label "#"}
     ; ::dt/render-fn (utils/get-id-link-formatter :edit-plant)}
    {::dt/column-key [:name] ::dt/column-label "Name"}
    {::dt/column-key [:status] ::dt/column-label "Status"}
    {::dt/column-key [:dataType] ::dt/column-label "Data Type"}
    {::dt/column-key [:logFrequency] ::dt/column-label "Log Frequency"}
    {::dt/column-key [:creationDate]
     ::dt/column-label "Creation Date"
     ::dt/render-fn utils/date-formatter}]
   {::dt/table-classes ["table__wrapper"]}])

(def sensors-page
  (reagent/create-class
   {:component-will-mount
    #(re-frame/dispatch [:get-all-sensors/request])
    :reagent-render
     (fn []
       [:div {:style {:display "flex"
                      :flex-direction "column"
                      :margin-left 25
                      :margin-right 25}}
         [:h2 "Sensors page"]
         [ui/raised-button {:type "button"
                            :label "Create new sensor"
                            :style {:width 256
                                    :margin-bottom 25}
                            :on-click #(router/navigate! :sensor-by-id {:id "new"})}]
         [sensors-table]])}))



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
  (let [firmware (re-frame/subscribe [:last-created-firmware])]
    (reagent/create-class
     {:component-will-unmount
      #(re-frame/dispatch [:clear-last-created-firmware])
      :reagent-render
        (fn []
          [:div {:style page-wrapper-style}
           [sensor-form id]
           [:div {:style {:width 256
                          :margin-top 15}}
             [clipboard-button "Copy Firmware Code" "#firmware" (empty? @firmware)]]
           [ui/text-field
            {:id "firmware"
             :multi-line true
             :rows-max 15
             :floating-label-text "Firmware"
             :value (or @firmware "")}]])})))
