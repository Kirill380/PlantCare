(ns plant-care-ui.pages.registration.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :refer [listen build-text-field-options]]
            [cljs-react-material-ui.reagent :as ui]
            [plant-care-ui.pages.registration.events]
            [plant-care-ui.pages.registration.subs]))

(defn error-text [state]
  {:error-text (or (:error-message state) "")})

(defn registration-form []
  (let [first-name (listen :registration-first-name)
        last-name (listen :registration-last-name)
        email (listen :registration-email)
        password (listen :registration-password)
        confirm-password (listen :registration-confirm-password)]
    (println email)
    [:form {:style {:display "flex"
                    :flex-direction "column"
                    :width 256}
            :on-submit (fn [e]
                         (.preventDefault e)
                         (println "lol" e)
                         (re-frame/dispatch [:registration-user/request]))}
     [ui/text-field
      (merge
       (error-text first-name)
       (build-text-field-options
        "First Name"
        :set-registration-first-name
        (:value first-name)))]

     [ui/text-field
      (merge
       (error-text last-name)
       (build-text-field-options
        "Last Name"
        :set-registration-last-name
        (:value last-name)))]
     [ui/text-field
      (merge
       (error-text email)
       (build-text-field-options
        "Email"
        :set-registration-email
        (:value email)))]
     [ui/text-field
      (merge
       (error-text password)
       (build-text-field-options
        "Password"
        :set-registration-password
        (:value password))
       {:type "password"})]
     [ui/text-field
      (merge
       (build-text-field-options
        "Confirm Password"
        :set-registration-confirm-password
        (:value confirm-password))
       {:type "password"})]
     [ui/raised-button {:type "submit"
                        :label "REGISTER"
                        :primary true}]]))


(defn registration-page []
  [:div {:style {:display "flex"
                 :flex-direction "column"
                 :margin-left 25}}
   [:h2 "Registration page"]
   [registration-form]])
