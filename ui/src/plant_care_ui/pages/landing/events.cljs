(ns plant-care-ui.pages.landing.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.config :as config]
            [goog.crypt.base64 :as base64]
            [clojure.string :as string]))

(re-frame/reg-event-db
 :landing-set-login
 [utils/common-interceptors]
 (fn [db [_ value]]
   (assoc-in db [:pages :landing :fields :login] value)))

(re-frame/reg-event-db
 :landing-set-password
 [utils/common-interceptors]
 (fn [db [_ value]]
   (assoc-in db [:pages :landing :fields :password] value)))

(defn ->login-body [{:keys [login password]}]
  {:email login
   :password password})

(re-frame/reg-event-fx
 :login-request
 [utils/common-interceptors]
 (fn [coefx event]
   (let [fields (get-in coefx [:db :pages :landing :fields])
         body (->login-body fields)]
     (println "body " body)
    {:http-xhrio {:method :post
                  :uri (str config/api-url "/auth/login")
                  :response-format (ajax/json-response-format {:keywords? true})
                  :format (ajax/json-request-format)
                  :params body
                  :on-success [:login-success]
                  :on-failure [:login-failure]}})))

; it's bad to use js/JSON not provided by coeffect
(defn parse-auth-token [token]
  (js->clj
   (.parse js/JSON
    (base64/decodeString
     (second
      (string/split token #"\."))))
   :keywordize-keys true))

(re-frame/reg-event-fx
 :login-success
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ {:keys [token refreshToken]}]]
  (let [{:keys [login]} (get-in db [:pages :landing :fields])
        roles (:roles (parse-auth-token token))
        admin? (boolean (some #{"admin"} roles))]
    {:db (-> db
             (assoc-in [:users :current :token] token)
             (assoc-in [:users :current :refresh-token] refreshToken)
             (assoc-in [:users :current :roles] roles)
             (assoc-in [:users :current :email] login))
     :router {:handler :users}
     :dispatch (when admin? [:get-all-users/request])})))

(re-frame/reg-event-fx
 :login-failure
 [utils/common-interceptors]
 (fn [coefx event]
  (let [registration-fields (get-in coefx [:db :pages :landing :fields])]
    (println registration-fields))))
