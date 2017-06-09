(ns plant-care-ui.db.events
  (:require [re-frame.core :as re-frame]
            [plant-care-ui.db.core :refer [default-values]]
            [plant-care-ui.utils.core :as utils]))

(re-frame/reg-event-fx
 :init-db
 (fn [{:keys [db]} _]
   {:db (merge default-values db)}))

(def token-label "plant-care.token")
(def refresh-token-label "plant-care.refresh-token")
(def email-label "plant-care.email")

(re-frame/reg-event-fx
 :load-tokens
 [utils/common-interceptors]
 (fn [{:keys [db]}]
   (let [storage js/localStorage
         token (.getItem storage token-label)
         refresh-token (.getItem storage refresh-token-label)
         email (.getItem storage email-label)
         {:keys [roles]} (utils/extract-user-roles token)]
     {:db (-> db
              (assoc-in [:users :current :token] token)
              (assoc-in [:users :current :refresh-token] token)
              (assoc-in [:users :current :email] email)
              (assoc-in [:users :current :roles] roles)
              (assoc-in [:users :current :logged?] true))})))

(re-frame/reg-event-fx
 :store-tokens
 [utils/common-interceptors]
 (fn [_ [_ {:keys [token refresh-token email]}]]
   (let [storage js/localStorage]
     (.setItem storage token-label token)
     (.setItem storage refresh-token-label refresh-token)
     (.setItem storage email-label email))))
