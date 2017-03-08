(ns plant-care-ui.pages.landing.core
  (:require [plant-care-ui.components.material :as m]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.pages.landing.events]
            [plant-care-ui.router.core :as router]))

(defn login-form []
  (let [*login (reagent/atom "")
        *password (reagent/atom "")]
    (fn []
      [:form {:style {:display "flex"
                      :flex-direction "column"}
              :on-submit (fn [e]
                           (.preventDefault e)
                           (re-frame/dispatch [:login-request]
                                         {:login @*login
                                          :password @*password}))}
       [m/text-field {:type "text"
                      :floating-label-text "Login"
                      :value @*login
                      :on-change #(reset! *login (-> % .-target .-value))}]
       [m/text-field {:type "password"
                      :floating-label-text "Password"
                      :value @*password
                      :on-change #(reset! *password (-> % .-target .-value))}]
       [m/raised-button {:type "submit"
                         :primary true
                         :label "LOGIN"}]])))

(defn landing-page []
  [:div {:style {:display "flex"
                 :flex-direction "column"
                 :justify-content "center"
                 :align-items "center"
                 :height 500
                 :width "100%"}}
   [login-form]
   [:div {:style {:margin-top 5
                  :width 256}}
    [m/raised-button {:primary true
                      :label "REGISTRATION"
                      :full-width true
                      :on-click #(router/navigate! :registration)}]]])
