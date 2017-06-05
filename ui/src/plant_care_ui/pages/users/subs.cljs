(ns plant-care-ui.pages.users.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :all-users-list
 (fn [db]
   (->> db
        :users
        :all)))

; (re-frame/reg-sub
;  :all-users-list
;  (fn [_ _]
;    (re-frame/subscribe [:all-users-map]))
;  (fn [users-map _]
;    (into [] (vals users-map))))

(re-frame/reg-sub
 :current-user/admin?
 (fn [db]
   (boolean
    (some #{"admin"}
     (->> db
         :users
         :current
         :roles)))))

(re-frame/reg-sub
 :edit-user-form
 (fn [db]
   (->> db
        :pages
        :edit-user
        :fields)))
