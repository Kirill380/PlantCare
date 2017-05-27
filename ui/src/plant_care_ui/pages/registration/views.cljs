(ns plant-care-ui.pages.registration.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :refer [listen build-text-field-options]]
            [cljs-react-material-ui.reagent :as ui]
            [plant-care-ui.pages.registration.events]
            [plant-care-ui.pages.registration.subs]))

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
                         (re-frame/dispatch [:registration-user/request]))}
     [ui/text-field
      (build-text-field-options
       "First Name"
       :set-registration-first-name
       first-name)]

     [ui/text-field
      (build-text-field-options
       "Last Name"
       :set-registration-last-name
       last-name)]
     [ui/text-field
      (build-text-field-options
       "Email"
       :set-registration-email
       email)]
     [ui/text-field
      (merge
       (build-text-field-options
        "Password"
        :set-registration-password
        password)
       {:type "password"})]
     [ui/text-field
      (merge
       (build-text-field-options
        "Confirm Password"
        :set-registration-confirm-password
        confirm-password)
       {:type "password"})]
     [ui/raised-button {:type "submit"
                        :label "REGISTER"
                        :primary true}]]))


(defn registration-page []
  [:div {:style {:display "flex"
                 :flex-direction "column"}}
   [:h2 "Registration page"]
   [registration-form]])
