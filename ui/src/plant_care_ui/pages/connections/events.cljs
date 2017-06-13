(ns plant-care-ui.pages.connections.events
  (:require [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.config :as config]
            [ajax.core :as ajax]))

(re-frame/reg-event-fx
  :get-plants-by-sensor/request
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ id]]
    (let [token (get-in db [:users :current :token])]
      {:http-xhrio {:method :get
                    :uri (str config/api-url "/api/sensors/" id "/plants")
                    :response-format (ajax/json-response-format)
                    :headers {"Authorization" (str "Bearer " token)}
                    :format (ajax/json-request-format)
                    :on-success [:get-plants-by-sensor/success id]
                    :on-failure [:get-plants-by-sensor/failure id]}})))

(re-frame/reg-event-fx
  :get-plants-by-sensor/success
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ id resp]]
    (println "success" id resp)))

(re-frame/reg-event-fx
  :get-plants-by-sensor/failure
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ v]]
    (println "failure" v)))

(re-frame/reg-event-fx
  :get-plants-for-present-sensors
  [utils/common-interceptors]
  (fn [{:keys [db]}]
    (let [token (get-in db [:users :current :token])
          sensor-ids (->> db
                       :sensors
                       :all
                       vals
                       (map :id))
          actions (map #(vector :get-plants-by-sensor/request %) sensor-ids)]
      {:dispatch-n actions})))

(re-frame/reg-event-fx
  :connect-sensor-to-plant/request
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ sensor-id plant-id]]
    (let [token (get-in db [:users :current :token])]
      {:http-xhrio {:method :put
                    :uri (str config/api-url "/api/sensors/" sensor-id "/plants/" plant-id)
                    :response-format (ajax/text-response-format)
                    :headers {"Authorization" (str "Bearer " token)}
                    :format (ajax/json-request-format)
                    :on-success [:connect-sensor-to-plant/success sensor-id plant-id]
                    :on-failure [:connect-sensor-to-plant/failure sensor-id plant-id]}})))

(re-frame/reg-event-fx
  :connect-sensor-to-plant/success
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ sensor-id plant-id]]
    {:dispatch [:app/show-message "Success!"]}))

(re-frame/reg-event-fx
  :connect-sensor-to-plant/failure
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ sensor-id plant-d]]
    {:dispatch [:app/show-message (str "Fail while connecting")]}))
