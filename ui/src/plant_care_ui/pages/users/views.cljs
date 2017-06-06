(ns plant-care-ui.pages.users.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.pages.users.subs]
            [plant-care-ui.pages.users.events]
            [re-frame-datatable.core :as dt]
            [plant-care-ui.router.nav :as router]
            [cljs-react-material-ui.reagent :as ui]))

(defn users-list []
  (let [users (utils/listen :all-users-list)]
    [:ul
      (for [user users]
        [:li {:key (:email user)}
             (:email user)])]))

(defn users-table []
 [dt/datatable
  :tables/all-users
  [:all-users-list]
  [{::dt/column-key [:id]
    ::dt/column-label "#"
    ::dt/sorting {::dt/enabled? true}
    ::dt/render-fn (utils/get-id-link-formatter :edit-user)}
   {::dt/column-key [:firstName] ::dt/column-label "First Name"}
   {::dt/column-key [:lastName] ::dt/column-label "Last Name"}
   {::dt/column-key [:email] ::dt/column-label "Email"}
   {::dt/column-key [:creationDate]
    ::dt/column-label "Creation Date"
    ::dt/render-fn utils/date-formatter}]
  {::dt/table-classes ["table__wrapper"]
   ::dt/pagination {::dt/enabled? true ::dt/per-page 10}}])

(def page-wrapper-style
  {:display "flex"
   :flex-direction "column"
   :margin-left 25
   :margin-right 25})

(defn user-form [user-id]
  (let [init-state (utils/listen :edit-user-form)
        form-state (reagent/atom init-state)
        on-change-field (fn [field]
                          (fn [e]
                            (swap! form-state assoc field (-> e .-target .-value))))]
    (fn [user-id]
      (println "form state" form-state)
      [:form {:style {:display "flex"
                      :flex-direction "column"
                      :width 256}
              :on-submit (fn [e]
                           (.preventDefault e)
                           (re-frame/dispatch [:edit-user/submit user-id @form-state]))}
       [ui/text-field
        {:floating-label-text "First name"
         :on-change (on-change-field :firstName)
         :value (:firstName @form-state)}]
       [ui/text-field
        {:floating-label-text "Last name"
         :on-change (on-change-field :lastName)
         :value (:lastName @form-state)}]
       [ui/text-field
        {:floating-label-text "Email"
         :on-change (on-change-field :email)
         :value (:email @form-state)}]
       [ui/raised-button {:type "submit"
                          :label "SAVE"
                          :primary true}]
       [ui/raised-button {:type "button"
                          :label "RESET"
                          :on-click #(reset! form-state init-state)}]
       [ui/raised-button {:type "button"
                          :label "DELETE"
                          :secondary true
                          :on-click #(re-frame/dispatch [:delete-user/request user-id])}]])))



(defn users-page []
  [:div {:style page-wrapper-style}
   [:h2 "Users page"]
   [users-table]])

(defn user-by-id-page [id]
  [:div {:style page-wrapper-style}
   [:h2 "User page " id]
   [user-form id]])
