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

(defn go-to-page [page-id]
  (toggle-drawer)
  (router/navigate! page-id))

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
                     :on-click #(go-to-page :users)}]
      [ui/menu
;;        [ui/menu-item {:primary-text "Dashboard"
;;                       :left-icon (icons/action-dashboard)}]
       [ui/menu-item {:primary-text "Sign In"
                      :on-click #(go-to-page :landing)}]
       [ui/menu-item {:primary-text "Sensors page"
                      :left-icon (icons/hardware-memory)}]
       [ui/menu-item {:primary-text "Flowers page"
                      :left-icon (icons/maps-local-florist)
                      :on-click #(go-to-page :flowers)}]
       [ui/menu-item {:primary-text "Connections page"
                      :left-icon (icons/action-settings-ethernet)}]])]])

(defn app [child]
  (let [drawer-open? (utils/listen :app/drawer-open?)
        message (:message (utils/listen :app/snackbar))
        admin? (utils/listen :current-user/admin?)
        logged? (utils/listen :logged?)]
   [:div
    [ui/app-bar {:title (str "Plant Care" (when admin? " Admin Panel"))
                 :on-left-icon-button-touch-tap toggle-drawer
                 :icon-element-right (when logged?
                                       (reagent/as-component
                                         [ui/flat-button
                                           {:on-click #(re-frame/dispatch [:log-out/request])}
                                           "Log out"]))}]
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

(defn loading-indicator []
  [ui/circular-progress {:size 60}])

(defn analytics-block [plant-id]
  [:div (str "chart will be here")
    [:div
      [ui/flat-button {:label "Last minute"}]
      [ui/flat-button {:label "Last hour"}]
      [ui/flat-button {:label "Last day"}]
      [ui/flat-button {:label "Last week"}]
      [ui/flat-button {:label "Last month"}]]])

(defn plant-card [{:keys [id]}]
  (let [plant (re-frame/subscribe [:plant-by-id id])
        analytics-shown? (reagent/atom false)]
    (fn [{:keys [id]}]
      (println "analytics" @analytics-shown?)
      [:div {:style {:margin-bottom 20}}
        [ui/card
         [ui/card-header
           {:title (:name @plant)
            :act-as-expander true
            :show-expandable-button true}]
         [ui/card-media
           [:div {:style {:display "flex"
                          :justify-content "center"}}
             [:img {:src (:image @plant)}]]]
         [ui/card-text
           {:expandable true}
           [:div
             [:div (str "ID: " (:id @plant))]
             [:div (str "Name: " (:name @plant))]
             [:div (str "Location: " (:location @plant "Not defined"))]
             [:div (str "Species: " (:species @plant "Not defined"))]
             [:div (str "Moisture Threshold: " (:moistureThreshold @plant "Not defined"))]
             [:div (str "Creation Date: " (:creationDate @plant))]]]
         [ui/card-actions {:expandable true}
           [ui/flat-button {:label "Edit"
                            :on-click #(re-frame/dispatch [:edit-plant id])}]
           [ui/flat-button {:label (if @analytics-shown?
                                     "Hide analytics"
                                     "Show analytics")
                            :on-click #(swap! analytics-shown? not)}]]
         (when @analytics-shown?
           [ui/card-text
            {:expandable true}
            [analytics-block id]])]])))
