(ns plant-care-ui.pages.registration.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [plant-care-ui.utils.core :refer [common-interceptors]]
            [plant-care-ui.config :as config]
            [plant-care-ui.utils.core :as utils]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :as case-format]))

(re-frame/reg-event-fx
 :registration-user/request
 [common-interceptors]
 (fn [coefx event]
   (let [fields (get-in coefx [:db :pages :registration :fields])
         mapped-fields (utils/map-values :value fields)
         body (transform-keys
                case-format/->camelCaseString
                (dissoc mapped-fields :confirm-password))]
    {:http-xhrio {:method :post
                  :uri (str config/api-url "/users")
                  :response-format (ajax/json-response-format {:keywords? true})
                  :format (ajax/json-request-format)
                  :params body
                  :on-success [:registration-user/success]
                  :on-failure [:registration-user/failure]}})))

(defn registration-user-success [db event]
  (println "SUCCESS" event)
  db)
(re-frame/reg-event-db
 :registration-user/success
 [common-interceptors]
 registration-user-success)

(defn prepared-errors [result error]
 (let [{:keys [fieldName errorMessage]} error]
   (assoc result
          (case-format/->kebab-case-keyword fieldName)
          errorMessage)))

(defn registration-user-failre [db [_ {:keys [response]}]]
  (let [field-errors (:fieldErrors response)
        prepared-errors (reduce prepared-errors {} field-errors)
        error-path #(vec [:pages :registration :fields % :error-message])]
    (-> db
         (assoc-in (error-path :first-name) (:first-name prepared-errors))
         (assoc-in (error-path :last-name) (:last-name prepared-errors))
         (assoc-in (error-path :email) (:email prepared-errors))
         (assoc-in (error-path :password) (:password prepared-errors)))))

(re-frame/reg-event-db
 :registration-user/failure
 [common-interceptors]
 registration-user-failre)

(defn reg-event-db-for-field [field id]
  (re-frame/reg-event-db
   id
   [common-interceptors]
   (fn [db [_ value]]
     (assoc-in db [:pages :registration :fields field :value] value))))

(reg-event-db-for-field
 :first-name
 :set-registration-first-name)

(reg-event-db-for-field
 :last-name
 :set-registration-last-name)

(reg-event-db-for-field
 :email
 :set-registration-email)

(reg-event-db-for-field
 :password
 :set-registration-password)

(reg-event-db-for-field
 :confirm-password
 :set-registration-confirm-password)
