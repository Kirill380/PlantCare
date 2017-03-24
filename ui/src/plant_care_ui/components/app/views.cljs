(ns plant-care-ui.components.app.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.components.material :as m]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.components.app.subs]
            [plant-care-ui.components.app.events]))

(def toggle-drawer #(re-frame/dispatch [:app/toggle-drawer]))

(defn navigation-header [props]
  (let [app-bar-theme (-> (js->clj props :keywordize-keys true)
                       :muiTheme
                       :appBar)
        color (-> app-bar-theme :color)
        text-color (-> app-bar-theme :textColor)
        height (-> app-bar-theme :height)]
   [m/paper {:z-depth 2
             :style {:background-color color}}
    [:div {:style {:display "flex"
                   :justify-content "center"
                   :align-items "center"
                   :background-color color
                   :color text-color
                   :height height
                   :font-size 21}}
      "Navigation"]]))

(def mui-navigation-header
  (utils/mui-themeable navigation-header))

(defn navigation []
  [:div
   [mui-navigation-header]
   [m/menu
    [m/menu-item {:primary-text "Dashboard"
                  :on-touch-tap #(println "go to dashboard")}]
    [m/menu-item {:primary-text "Sensors page"}]
    [m/menu-item {:primary-text "Flowers page"}]
    [m/menu-item {:primary-text "Connections page"}]]])

(defn app [child]
  (let [open? (utils/listen :app/drawer-open?)]
   [:div
    [m/app-bar {:title "Title"
                :on-left-icon-button-touch-tap toggle-drawer}]
    [m/drawer {:docked false
               :open open?
               :on-request-change toggle-drawer}
     [navigation]]
    child]))
