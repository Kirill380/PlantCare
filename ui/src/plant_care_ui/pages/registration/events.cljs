(ns plant-care-ui.pages.registration.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [plant-care-ui.utils.core :refer [common-interceptors]]
            [plant-care-ui.config :as config]))

(re-frame/reg-event-fx
 :registration-user/request
 [common-interceptors]
 (fn [coefx event]
   (println "db" (get-in coefx [:db :pages :registration :fields]))
   {:http-xhrio {:method :post
                 :uri (str config/api-url "/users")
                 :response-format (ajax/json-response-format {:keywords? true})
                 :format (ajax/json-request-format)
                 :params (get-in coefx [:db :pages :registration :fields])
                 :on-success [:registration-user/success]
                 :on-failure [:registration-user/failure]}}))

(defn registration-user-success [db event]
  (println "SUCCESS" event))
(re-frame/reg-event-db
 :registration-user/success
 [common-interceptors]
 registration-user-success)

(defn registration-user-failre [db event]
  (println "FAILURE" event))
(re-frame/reg-event-db
 :registration-user/failure
 [common-interceptors]
 registration-user-failre)

(defn reg-event-db-for-field [field id]
  (re-frame/reg-event-db
   id
   [common-interceptors]
   (fn [db [_ value]]
     (assoc-in db [:pages :registration :fields field] value))))

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
