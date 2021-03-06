(ns plant-care-ui.pages.flowers.events
  (:require [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.config :as config]
            [ajax.core :as ajax]))


(re-frame/reg-event-fx
  :get-all-flowers/request
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ v]]
    (let [token (get-in db [:users :current :token])]
      {:http-xhrio {:method :get
                    :uri (str config/api-url "/api/plants")
                    :response-format (ajax/json-response-format {:keywords? true})
                    :headers {"Authorization" (str "Bearer " token)}
                    :format (ajax/json-request-format)
                    :on-success [:get-all-flowers/success]
                    :on-failure [:get-all-flowers/failure]}})))

(defn map-plant [[id plant-arr]]
  (let [plant (first plant-arr)
        image (:image plant)]
    (if (not (nil? image))
      (vector
        id
        (assoc plant :image (utils/base64<-from-server (:image plant))))
      plant)))

(re-frame/reg-event-db
  :get-all-flowers/success
  [utils/common-interceptors]
  (fn [db [_ v]]
    (let [plants (:items v)
          grouped (group-by :id plants)
          mapped (into {} (map map-plant grouped))]
     (assoc-in db [:flowers :all] mapped))))

(re-frame/reg-event-fx
  :get-all-flowers/failure
  [utils/common-interceptors]
  (fn [db [_ v]]
    {:dispatch [:app/show-message (str "Fail! " (-> v :response :message))]}))

(re-frame/reg-event-fx
  :edit-plant
  [utils/common-interceptors]
  (fn [_ [_ v]]
    {:router {:handler :plant-by-id
              :params {:id v}}}))

(re-frame/reg-event-fx
  :edit-plant/request
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ id fields]]
    (let [token (get-in db [:users :current :token])]
      {:http-xhrio {:method :put
                    :uri (str config/api-url "/api/plants/" id)
                    :response-format (ajax/text-response-format)
                    :headers {"Authorization" (str "Bearer " token)}
                    :format (ajax/json-request-format)
                    :params fields
                    :on-success [:edit-plant/success]
                    :on-failure [:edit-plant/failure]}})))

(re-frame/reg-event-fx
  :edit-plant/success
  [utils/common-interceptors]
  (fn [_]
    {:dispatch [:app/show-message "Plant successfully updated"]}))

(re-frame/reg-event-fx
  :edit-plant/failure
  [utils/common-interceptors]
  (fn [_ [_ v]]
    {:dispatch [:app/show-message (str "Error! " (-> v :response :message))]}))

(re-frame/reg-event-fx
  :delete-plant/request
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ id]]
    (let [token (get-in db [:users :current :token])]
      {:http-xhrio {:method :delete
                    :uri (str config/api-url "/api/plants/" id)
                    :response-format (ajax/text-response-format)
                    :headers {"Authorization" (str "Bearer " token)}
                    :format (ajax/json-request-format)
                    :on-success [:delete-plant/success id]
                    :on-failure [:delete-plant/failure]}})))

(re-frame/reg-event-fx
  :delete-plant/success
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ id]]
    {:db (update-in db [:flowers :all] dissoc (js/parseInt id))
     :dispatch [:app/show-message "Plant successfully deleted"]
     :router {:handler :flowers}}))

(re-frame/reg-event-fx
  :delete-plant/failure
  [utils/common-interceptors]
  (fn [_ [_ v]]
    {:dispatch [:app/show-message (str "Error! " (:message (:response v)))]}))

(re-frame/reg-event-fx
  :create-plant/request
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ fields]]
    (let [token (get-in db [:users :current :token])]
      {:http-xhrio {:method :post
                    :uri (str config/api-url "/api/plants")
                    :response-format (ajax/json-response-format)
                    :headers {"Authorization" (str "Bearer " token)}
                    :format (ajax/json-request-format)
                    :params fields
                    :on-success [:create-plant/success]
                    :on-failure [:create-plant/failure]}})))

(re-frame/reg-event-fx
  :create-plant/success
  [utils/common-interceptors]
  (fn [_ [_ v]]
    {:router {:handler :flowers}
     :dispatch [:app/show-message "Success!"]}))

(re-frame/reg-event-fx
  :create-plant/failure
  [utils/common-interceptors]
  (fn [_ [_ v]]
    {:dispatch [:app/show-message (str "Error! " (:message (:response v)))]}))

(def time-range-hash
  {:last-minute (* 60 1000)
   :last-hour (* 60 60 1000)
   :last-day (* 24 60 60 1000)
   :last-week (* 7 24 60 60 1000)
   :last-month (* 31 24 60 60 1000)})

(defn get-from-to [time-range]
  (let [past-ms (get time-range-hash time-range)
        to (+ (.now js/Date) (* 3 60 60 1000))
        from (- to past-ms)
        to-formatted (utils/Date->cool-format (js/Date. to))
        from-formatted (utils/Date->cool-format (js/Date. from))]
    {:from from-formatted
     :to to-formatted}))

(re-frame/reg-event-fx
  :fetch-raw-sensor-data/request
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ plant-id time-range]]
    (println (get-from-to time-range))
    (let [token (get-in db [:users :current :token])]
      {:http-xhrio {:method :get
                    :uri (str config/api-url "/api/sensors/data")
                    :params (merge
                              (when time-range (get-from-to time-range))
                              {:plantId plant-id})
                    :response-format (ajax/json-response-format {:keywords? true})
                    :format (ajax/json-request-format)
                    :headers {"Authorization" (str "Bearer " token)}
                    :on-success [:fetch-raw-sensor-data/success plant-id]
                    :on-failure [:fetch-raw-sensor-data/failure]}})))

(re-frame/reg-event-fx
  :fetch-raw-sensor-data/success
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ plant-id resp]]
    (let [data (:dataTimeSeries resp)]
      (println data)
      {:db (update-in db [:raw-data] assoc (js/parseInt plant-id) data)})))

(re-frame/reg-event-fx
  :fetch-raw-sensor-data/failure
  [utils/common-interceptors]
  (fn [{:keys [db]} [_ resp]]
    (println "failure" resp)))
