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
     (println "token " token)
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
