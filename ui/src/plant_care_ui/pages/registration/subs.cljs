(ns plant-care-ui.pages.registration.subs
  (:require [re-frame.core :as re-frame]))

(defn reg-sub-for-field [field id]
  (re-frame/reg-sub
   id
   (fn [db _]
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

(reg-sub-for-field
 :email
 :registration-email)

(reg-sub-for-field
 :password
 :registration-password)

(reg-sub-for-field
 :confirm-password
 :registration-confirm-password)
