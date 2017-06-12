(ns plant-care-ui.pages.sensors.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [plant-care-ui.config :as config]
            [plant-care-ui.utils.core :as utils]))

(re-frame/reg-event-fx
 :create-sensor/request
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ v]]
   (println "value " v)
   (let [token (get-in db [:users :current :token])
         body {:name (:name v)
               :wifiName (:wifi-name v)
               :wifiPassword (:password v)
               :dataType "moisture"
               :logFrequency (js/parseInt (:frequency v))}]
     {:http-xhrio {:method :post
                   :uri (str config/api-url "/api/sensors")
                   :response-format (ajax/text-response-format)
                   :headers {"Authorization" (str "Bearer " token)}
                   :format (ajax/json-request-format)
                   :params body
                   :on-success [:create-sensor/success]
                   :on-failure [:create-sensor/failure]}})))

(re-frame/reg-event-fx
 :create-sensor/success
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ v]]
   {:db (update-in db [:firmwares] assoc :last-created v)}))

(re-frame/reg-event-fx
 :create-sensor/failure
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ v]]
   (println "failure" v)))


(re-frame/reg-event-fx
 :get-all-sensors/request
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ v]]
   (let [token (get-in db [:users :current :token])]
     {:http-xhrio {:method :get
                   :uri (str config/api-url "/api/sensors")
                   :response-format (ajax/json-response-format {:keywords? true})
                   :headers {"Authorization" (str "Bearer " token)}
                   :format (ajax/json-request-format)
                   :on-success [:get-all-sensors/success]
                   :on-failure [:get-all-sensors/failure]}})))

(defn map-sensor [[id sensor-arr]]
  (let [sensor (first sensor-arr)]
    (vector
      id
      sensor)))

(re-frame/reg-event-fx
 :get-all-sensors/success
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ v]]
   (let [sensors (:items v)
         grouped (group-by :id sensors)
         mapped (into {} (map map-sensor grouped))]
     {:db (assoc-in db [:sensors :all] mapped)})))

(re-frame/reg-event-fx
 :get-all-sensors/failure
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ v]]
   (println "failure" v)))

(re-frame/reg-event-db
 :clear-last-created-firmware
 [utils/common-interceptors]
 (fn [db]
   (assoc-in db [:firmwares :last-created] "")))
