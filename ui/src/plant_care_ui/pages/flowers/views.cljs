(ns plant-care-ui.pages.flowers.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame-datatable.core :as dt]
            [plant-care-ui.router.nav :as router]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.pages.flowers.events]
            [plant-care-ui.pages.flowers.subs]))
;;             [plant-care-ui.pages.users.events]


(defn flowers-table []
  [dt/datatable
   :tables/all-flowers
   [:all-flowers-list]
   [{::dt/column-key [:id]
     ::dt/column-label "#"
     ::dt/render-fn (utils/get-id-link-formatter :edit-plant)}
    {::dt/column-key [:name] ::dt/column-label "Name"}
    {::dt/column-key [:species] ::dt/column-label "Species"}
    {::dt/column-key [:location] ::dt/column-label "Location"}
    {::dt/column-key [:creationDate]
     ::dt/column-label "Creation Date"
     ::dt/render-fn utils/date-formatter}
    {::dt/column-key [:moistureThreshold] ::dt/column-label "Threshold"}]
   {::dt/table-classes ["table__wrapper"]}])

(def page-wrapper-style
 {:display "flex"
  :flex-direction "column"
  :margin-left 25
  :margin-right 25})

(defn flowers-page []
  (reagent/create-class
    {:component-will-mount #(re-frame/dispatch [:get-all-flowers/request])
     :reagent-render
      (fn []
        [:div {:style page-wrapper-style}
          [:h2 "Plants page!"]
          [ui/raised-button {:type "button"
                             :label "Create new plant"
                             :style {:width 256
                                     :margin-bottom 25}
                             :on-click #(router/navigate! :plant-by-id {:id :new})}]

          [flowers-table]])}))

(defn flower-form [flower-id]
  (let [init-state (when (not= flower-id "new")
                     (re-frame/subscribe [:plant-by-id flower-id]))
        form-state (reagent/atom
                     (if (= flower-id "new")
                       (hash-map)
                       @init-state))
        on-change-field (fn [field]
                          (fn [e]
                            (swap! form-state assoc field (-> e .-target .-value))))]
    (fn [flower-id]
      (println "form state" @form-state)
      [:form {:style {:display "flex"
                      :flex-direction "column"
                      :width 256}
              :on-submit (fn [e]
                           (.preventDefault e)
                           (re-frame/dispatch
                             (if (= flower-id "new")
                               [:create-plant/request @form-state]
                               [:edit-plant/request flower-id @form-state])))}
        [ui/text-field
          {:floating-label-text "Name"
           :on-change (on-change-field :name)
           :value (:name @form-state)}]
        [ui/text-field
          {:floating-label-text "Species"
           :on-change (on-change-field :species)
           :value (:species @form-state)}]
        [ui/text-field
          {:floating-label-text "Location"
           :on-change (on-change-field :location)
           :value (:location @form-state)}]
        [ui/text-field
          {:floating-label-text "Threshold"
           :on-change (on-change-field :moistureThreshold)
           :value (:moistureThreshold @form-state)}]

        [ui/raised-button {:type "submit"
                           :label (if (= flower-id "new")
                                    "CREATE"
                                    "SAVE")
                           :primary true}]
        (when (not= flower-id "new")
          [ui/raised-button {:type "button"
                             :label "RESET"
                             :on-click #(reset! form-state @init-state)}])
        (when (not= flower-id "new")
          [ui/raised-button {:type "button"
                             :label "DELETE"
                             :secondary true
                             :on-click #(re-frame/dispatch [:delete-plant/request flower-id])}])])))

(defn flower-by-id-page [id]
  (reagent/create-class
    {:component-will-mount #(re-frame/dispatch [:get-all-flowers/request])
     :reagent-render
      (fn [id]
        [:div {:style page-wrapper-style}
          [:h2 (if (= id "new")
                 "Create New Plant"
                 (str "Flower page " id))]
          [flower-form id]])}))
