(ns plant-care-ui.pages.registration.subs
  (:require [re-frame.core :as re-frame]))

(defn reg-sub-for-field [field id]
  (re-frame/reg-sub
   id
   (fn [db _]
     (println "sub for " field " calculated" db)
     (->> db
          :pages
          :registration
          :fields
          field))))

(reg-sub-for-field
 :first-name
 :registration-first-name)

(reg-sub-for-field
 :last-name
 :registration-last-name)