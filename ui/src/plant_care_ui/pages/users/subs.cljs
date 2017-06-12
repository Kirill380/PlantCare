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
 :current-user/user?
 (fn [db]
   (boolean
    (some #{"regularUser"}
     (->> db
          :users
          :current
          :roles)))))

(re-frame/reg-sub
 :current-user-roles
 (fn [db]
   (or
     (->> db
          :users
          :current
          :roles)
     [:no-roles])))

(re-frame/reg-sub
 :edit-user-form
 (fn [db]
   (->> db
        :pages
        :edit-user
        :fields)))

(re-frame/reg-sub
 :user/logged?
 (fn [db]
   (->> db
        :users
        :current
        :logged?)))
