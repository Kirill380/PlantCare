(ns plant-care-ui.pages.registration.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.components.material :as m]
            [plant-care-ui.pages.registration.events]
            [plant-care-ui.pages.registration.subs]))

(defn registration-form []
  (let [first-name @(re-frame/subscribe [:registration-first-name])
        last-name @(re-frame/subscribe [:registration-last-name])
        email @(re-frame/subscribe [:registration-email])]
    (println "first nme" first-name)
    [:form {:style {:display "flex"
                    :flex-direction "column"
                    :width 256}
            :on-submit (fn [e]
                         (.preventDefault e)
                         (re-frame/dispatch [:register-request]))}
     [m/text-field {:floating-label-text "First Name"
                    :on-change #(re-frame/dispatch
                                 [:set-registration-first-name
                                  (-> % .-target .-value)])
                    :value first-name}]
     [m/text-field {:floating-label-text "Last Name"
                    :on-change #(re-frame/dispatch
                                 [:set-registration-last-name
                                  (-> % .-target .-value)])
                    :value last-name}]
     [m/text-field {:floating-label-text "Email"}]
     [m/text-field {:floating-label-text "Password"
                    :type "password"}]
     [m/text-field {:floating-label-text "Confirm Password"
                    :type "password"}]
     [m/raised-button {:type "submit"
                       :label "REGISTER"
                       :primary true}]]))


(defn registration-page []
  [:div {:style {:display "flex"
                 :flex-direction "column"}}
   "Registration Page !"
   [registration-form]])
