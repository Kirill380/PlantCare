(ns plant-care-ui.pages.users.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            ; [plant-care-ui.router.nav :as nav]
            [plant-care-ui.config :as config]
            [plant-care-ui.utils.core :as utils]))
            ; [camel-snake-kebab.extras :refer [transform-keys]]
            ; [camel-snake-kebab.core :as case-format]))

(re-frame/reg-event-fx
 :get-all-users/request
 [utils/common-interceptors]
 (fn [{:keys [db]} _]
   (let [token (get-in db [:users :current :token])]
    {:http-xhrio {:method :get
                  :uri (str config/api-url "/api/users")
                  :response-format (ajax/json-response-format {:keywords? true})
                  :headers {"Authorization" (str "Bearer " token)}
                  :format (ajax/json-request-format)
                  :on-success [:get-all-users/success]
                  :on-failure [:get-all-users/failure]}})))

(defn get-all-users-success [db [_ v]]
 (let [{:keys [items]} v]
  (assoc-in db [:users :all] items)))

(re-frame/reg-event-db
 :get-all-users/success
 [utils/common-interceptors]
 get-all-users-success)

(defn get-all-users-failure [coefx event]
  (println "FAILURE" coefx event))

(re-frame/reg-event-fx
 :get-all-users/failure
 [utils/common-interceptors]
 get-all-users-failure)

(defn edit-user [{:keys [db]} [_ v]]
  ; (println "EDIT USER" coefx v)
  (let [users (get-in db [:users :all])
        edited-user (first (filter #(= (:id %) v) users))]
    {:db (assoc-in db [:pages :edit-user :fields] edited-user)
     :router {:handler :user-by-id
              :params {:id v}}}))


(re-frame/reg-event-fx
 :edit-user
 [utils/common-interceptors]
 edit-user)

(defn reg-event-db-for-field [field id]
  (re-frame/reg-event-db
   id
   [utils/common-interceptors]
   (fn [db [_ value]]
     (assoc-in db [:pages :edit-user :fields field] value))))

(reg-event-db-for-field
 :firstName
 :edit-user-first-name)

(reg-event-db-for-field
 :lastName
 :edit-user-last-name)

(reg-event-db-for-field
 :email
 :edit-user-email)

(defn edit-user-reset [db [_ v]]
  (let [users (get-in db [:users :all])
        user (first (filter #(= (js/parseInt (:id %)) v) users))]
    (println "user" user)
    (assoc-in db [:pages :edit-user :fields] user)))

(re-frame/reg-event-db
 :edit-user/reset
 [utils/common-interceptors]
 edit-user-reset)

(defn edit-user-submit [{:keys [db]} [_ id fields]]
  (let [users (get-in db [:users :all])
        user (first (filter #(= (js/parseInt (:id %)) id) users))
        index (.indexOf users user)
        token (get-in db [:users :current :token])]
    (println "fields" fields)
  ; {:db (update-in db [:users :all index] merge fields)
   {:http-xhrio {:method :put
                 :uri (str config/api-url "/api/users/" id)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :headers {"Authorization" (str "Bearer " token)}
                 :format (ajax/json-request-format)
                 :params fields
                 :on-success [:edit-user/success]
                 :on-failure [:edit-user/failure]}}))

(re-frame/reg-event-fx
 :edit-user/submit
 [utils/common-interceptors]
 edit-user-submit)

(re-frame/reg-event-db
 :edit-user/success
 (fn [db]
   (println "SUCCESS")
   db))

(re-frame/reg-event-db
 :edit-user/failure
 (fn [db]
   (println "FAILURE")
   db))

(re-frame/reg-event-fx
 :delete-user/request
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ id]]
   (let [token (get-in db [:users :current :token])]
     {:http-xhrio {:method :delete
                   :uri (str config/api-url "/api/users/" id)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :headers {"Authorization" (str "Bearer " token)}
                   :format (ajax/json-request-format)
                   :on-success [:delete-user/success id]
                   :on-failure [:delete-user/failure id]}})))

(re-frame/reg-event-fx
 :delete-user/success
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ id]]
   (println "id" id)
   (let [users (get-in db [:users :all])
         new-users (filter #(not= (js/parseInt (:id %)) (js/parseInt id)) users)]
     {:router {:handler :users}
      :db (assoc-in db [:users :all] new-users)})))

(re-frame/reg-event-db
 :delete-user/failure
 [utils/common-interceptors]
 (fn [db]
   (println "FAILURE REMOVE")
   db))
