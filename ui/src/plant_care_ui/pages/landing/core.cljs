(ns plant-care-ui.pages.landing.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.components.material :as m]
            [plant-care-ui.router.nav :as router]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.pages.landing.events]
            [plant-care-ui.pages.landing.subs]))

(defn login-form []
  (let [login (utils/listen :landing-login)
        password (utils/listen :landing-password)]
      [:form {:style {:display "flex"
                      :flex-direction "column"}
              :on-submit (fn [e]
                           (.preventDefault e)
                           (re-frame/dispatch [:login-request]))}
       [m/text-field
        (utils/build-text-field-options
         "Login"
         :landing-set-login
         login)]

       [m/text-field
        (merge
         (utils/build-text-field-options
          "Password"
          :landing-set-password
          password)
         {:type "password"})]
       [m/raised-button {:type "submit"
                         :primary true
                         :label "LOGIN"}]]))

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
