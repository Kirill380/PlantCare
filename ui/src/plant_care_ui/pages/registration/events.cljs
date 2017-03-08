(ns plant-care-ui.pages.registration.events
  (:require [re-frame.core :as re-frame]))

(defn reg-event-db-for-field [field id]
  (re-frame/reg-event-db
   id
   []
   (fn [db [_ value]]
     (println field value)
     (assoc-in db [:pages :registration :fields field] value))))

(reg-event-db-for-field
 :first-name
 :set-registration-first-name)

(reg-event-db-for-field
 :last-name
 :set-registration-last-name)
