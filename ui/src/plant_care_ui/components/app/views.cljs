(ns plant-care-ui.components.app.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as icons]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.components.app.subs]
            [plant-care-ui.components.app.events]
            [plant-care-ui.router.nav :as router]))

(def toggle-drawer #(re-frame/dispatch [:app/toggle-drawer]))

(defn navigation-header [props]
  (let [app-bar-theme (-> (js->clj (get-mui-theme) :keywordize-keys true) :appBar)
        color (:color app-bar-theme)
        text-color (:textColor app-bar-theme)
        height (:height app-bar-theme)]
   [ui/paper {:z-depth 2
              :style {:background-color color}}
    [:div {:style {:display "flex"
                   :justify-content "center"
                   :align-items "center"
                   :background-color color
                   :color text-color
                   :height height
                   :font-size 21}}
      "Navigation"]]))

(defn navigation []
  [:div
   [navigation-header]
   [ui/menu
    (if-let [admin? (utils/listen :current-user/admin?)]
      [ui/menu-item {:primary-text "Users"
                     :left-icon (icons/social-group)
                     :on-click #(router/navigate! :users)}]
      [ui/menu
       [ui/menu-item {:primary-text "Dashboard"
                      :left-icon (icons/action-dashboard)}]
       [ui/menu-item {:primary-text "Sensors page"
                      :left-icon (icons/hardware-memory)}]
       [ui/menu-item {:primary-text "Flowers page"
                      :left-icon (icons/maps-local-florist)}]
       [ui/menu-item {:primary-text "Connections page"
                      :left-icon (icons/action-settings-ethernet)}]])]])

(defn app [child]
  (let [drawer-open? (utils/listen :app/drawer-open?)
        message (:message (utils/listen :app/snackbar))
        admin? (utils/listen :current-user/admin?)]
   [:div
    [ui/app-bar {:title (str "Plant Care" (when admin? " Admin Panel"))
                 :on-left-icon-button-touch-tap toggle-drawer}]
    [ui/drawer {:docked false
                :open drawer-open?
                :on-request-change toggle-drawer}
     [navigation]]
    (when (boolean message)
      [ui/snackbar {:open (boolean message)
                    :message message
                    :auto-hide-duration 3000
                    :on-request-close #(re-frame/dispatch [:app/hide-snackbar])}])
    child]))
