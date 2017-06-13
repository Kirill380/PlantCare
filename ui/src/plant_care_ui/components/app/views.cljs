(ns plant-care-ui.components.app.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljs-react-material-ui.core :refer [get-mui-theme color]]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as icons]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.components.app.subs]
            [plant-care-ui.components.app.events]
            [plant-care-ui.router.nav :as router]
            [cljsjs.clipboard]
            [cljsjs.moment]
            [cljsjs.chartjs]))


(def toggle-drawer #(re-frame/dispatch [:app/toggle-drawer]))

(defn go-to-page [page-id]
  (toggle-drawer)
  (router/navigate! page-id))

(def menu-settings
  [{:key :profile
    :props {:primary-text "Profile"
            ; :left-icon (icons/social-person)
            :on-click #(go-to-page :profile)}
    :available-for #{"admin" "regularUser"}}
   {:key :users
    :props {:primary-text "Users"
            ; :left-icon (icons/social-group)
            :on-click #(go-to-page :users)}
    :available-for #{"admin"}}
   {:key :plants
    :props {:primary-text "Plants"
            ; :left-icon (icons/maps-local-florist)
            :on-click #(go-to-page :flowers)}
    :available-for #{"regularUser"}}
   {:key :sensors
    :props {:primary-text "Sensors"
            ; :left-icon (icons/hardware-memory)
            :on-click #(go-to-page :sensors)}
    :available-for #{"regularUser"}}
   {:key :connections
    :props {:primary-text "Connections"
            :on-click #(go-to-page :connections)}
    :available-for #{"regularUser"}}])

; [ui/menu-item {:primary-text "Connections page"
               ; :left-icon (icons/action-settings-ethernet)}})

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
     (let [roles (utils/listen :current-user-roles)]
       (for [item menu-settings]
         ^{:key (:key item)}
         (let [available-for (:available-for item)
               available? (boolean (some available-for roles))
               props (:props item)]
           (when available?
             [ui/menu-item ^{:key item} props]))))]])

(defn app [child]
  (let [drawer-open? (utils/listen :app/drawer-open?)
        message (:message (utils/listen :app/snackbar))
        admin? (utils/listen :current-user/admin?)
        logged? (utils/listen :user/logged?)]
   [:div
    [ui/app-bar {:title (str "Plant Care" (when admin? " Admin Panel"))
                 :on-left-icon-button-touch-tap (when logged? toggle-drawer)
                 :icon-element-right (when logged?
                                       (reagent/as-component
                                         [ui/flat-button
                                           {:on-click (fn []
                                                        (when drawer-open? (toggle-drawer))
                                                        (re-frame/dispatch [:log-out/request]))}
                                           "Log out"]))}]
    (when logged?
      [ui/drawer {:docked false
                  :open drawer-open?
                  :on-request-change toggle-drawer}
        [navigation]])
    (when (boolean message)
      [ui/snackbar {:open (boolean message)
                    :message message
                    :auto-hide-duration 3000
                    :on-request-close #(re-frame/dispatch [:app/hide-snackbar])}])
    child]))

(defn loading-indicator []
  [ui/circular-progress {:size 60}])

(defn map-time-series-datum [{:keys [timestamp value]}]
  {:x (js/Date. timestamp)
   :y value})


(defn map-time-series-data [dataset]
  (map map-time-series-datum dataset))

(defn show-chart [elem-id data]
  (let [context (.getContext (.getElementById js/document elem-id) "2d")
        chart-data {:type "line"
                    :data {:datasets [{:data data
                                       :backgroundColor "lightgreen"
                                       :pointBackgroundColor "green"}]}
                    :options
                      {:scales
                        {:xAxes [{:display true
                                  :type "time"
                                  :scaleLabel {:display true
                                               :labelString "Date"}}]
                         :yAxes [{:display true
                                  :scaleLabel {:display true
                                               :labelString "Value"}}]}}}]
    (js/Chart. context (clj->js chart-data))))


(defn chart [data {:keys [id]}]
  (reagent/create-class
    {:component-did-mount #(show-chart id data)
     :component-did-update #(show-chart id data)
     :display-name "chart"
     :reagent-render
       (fn [data {:keys [id]}]
         [:canvas {:id id
                   :width 300
                   :height 300}])}))

(defn analytics-block [plant-id]
  (let [create-fetch-raw-data-fn (fn [plant-id time-range]
                                   (fn []
                                     (re-frame/dispatch [:fetch-raw-sensor-data/request plant-id time-range])))
        raw-data (re-frame/subscribe [:raw-data-for-plant plant-id])
        mapped-data (map-time-series-data @raw-data)]
    [:div
      (if (empty? mapped-data)
        [:div {:style {:display "flex"
                       :justify-content "center"}}
          "No data for selected period"]
        [:div {:style {:display "flex"
                       :justify-content "center"}}
          [:div {:style {:width "50%"}}
            [chart mapped-data {:id "chart_id"}]]])
      [:div
        [ui/flat-button {:label "Last minute"
                         :on-click (create-fetch-raw-data-fn plant-id :last-minute)}]
        [ui/flat-button {:label "Last hour"
                         :on-click (create-fetch-raw-data-fn plant-id :last-hour)}]
        [ui/flat-button {:label "Last day"
                         :on-click (create-fetch-raw-data-fn plant-id :last-day)}]
        [ui/flat-button {:label "Last week"
                         :on-click (create-fetch-raw-data-fn plant-id :last-week)}]
        [ui/flat-button {:label "Last month"
                         :on-click (create-fetch-raw-data-fn plant-id :last-month)}]]]))

(defn plant-card [{:keys [id]}]
  (let [plant (re-frame/subscribe [:plant-by-id id])
        card-expanded? (reagent/atom false)
        toggle-card-expand #(swap! card-expanded? not)
        analytics-shown? (reagent/atom false)]
    (fn [{:keys [id]}]
      [:div {:style {:margin-bottom 20}}
        [ui/card {:expanded @card-expanded?
                  :on-expand-change toggle-card-expand}
         [ui/card-header
           {:title (:name @plant)
            :act-as-expander true
            :show-expandable-button true}]
         [ui/card-media
           [:div {:style {:display "flex"
                          :justify-content "center"}}
             [:img {:src (:image @plant)}]]]
         [ui/card-actions
           [ui/flat-button
             {:label "Toggle Card Info"
              :on-click toggle-card-expand}]]
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


(defn clipboard-button [label target disabled?]
  (let [clipboard-atom (atom nil)]
    (reagent/create-class
     {:display-name "clipboard-button"
      :component-did-mount
      #(let [clipboard (new js/Clipboard (reagent/dom-node %))]
         (reset! clipboard-atom clipboard))
      :component-will-unmount
      #(when-not (nil? @clipboard-atom)
         (.destroy @clipboard-atom)
         (reset! clipboard-atom nil))
      :reagent-render
      (fn [label target disabled?]
        [ui/flat-button
         {:label label
          :disabled disabled?
          :readOnly true
          :full-width true
          :secondary true
          :class "clipboard"
          :data-clipboard-target target}])})))
