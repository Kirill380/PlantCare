(ns plant-care-ui.pages.registration.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :refer [listen]]
            [plant-care-ui.components.material :as m]
            [plant-care-ui.pages.registration.events]
            [plant-care-ui.pages.registration.subs]))

(defn build-text-field-options [label dispatch-key value]
  {:floating-label-text label
   :value value
   :on-change #(re-frame/dispatch
                [dispatch-key
                 (-> % .-target .-value)])})

(defn registration-form []
  (let [first-name (listen :registration-first-name)
        last-name (listen :registration-last-name)
        email (listen :registration-email)
        password (listen :registration-password)
        confirm-password (listen :registration-confirm-password)]
    [:form {:style {:display "flex"
                    :flex-direction "column"
                    :width 256}
            :on-submit (fn [e]
                         (.preventDefault e)
                         (re-frame/dispatch [:register-request]))}
     [m/text-field
      (build-text-field-options
       "First Name"
       :set-registration-first-name
       first-name)]

     [m/text-field
      (build-text-field-options
       "Last Name"
       :set-registration-last-name
       last-name)]
     [m/text-field
      (build-text-field-options
       "Email"
       :set-registration-email
       email)]
     [m/text-field
      (merge
       (build-text-field-options
        "Password"
        :set-registration-password
        password)
       {:type "password"})]
     [m/text-field
      (merge
       (build-text-field-options
        "Confirm Password"
        :set-registration-confirm-password
        confirm-password)
       {:type "password"})]
     [m/raised-button {:type "submit"
                       :label "REGISTER"
                       :primary true}]]))


(defn registration-page []
  [:div {:style {:display "flex"
                 :flex-direction "column"}}
   "Registration Page !"
   [registration-form]])
