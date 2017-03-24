(ns plant-care-ui.pages.landing.subs
  (:require [re-frame.core :as re-frame]))

(defn reg-sub-for-field [field id]
  (re-frame/reg-sub
   id
   (fn [db _]
     (->> db
          :pages
          :landing
          :fields
          field))))

(reg-sub-for-field
 :login
 :landing-login)

(reg-sub-for-field
 :password
 :landing-password)
