(ns plant-care-ui.pages.registration.events
  (:require [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :refer [common-interceptors]]))

(re-frame/reg-event-fx
 :register-request
 [common-interceptors]
 (fn [coefx event]
   (println "db" (get-in coefx [:db :pages :registration :fields]))))

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
