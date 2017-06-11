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
    {:db (update-in db [:flowers :all (js/parseInt id)] dissoc)
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
