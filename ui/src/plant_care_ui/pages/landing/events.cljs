(ns plant-care-ui.pages.landing.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [plant-care-ui.utils.core :as utils]
            [plant-care-ui.config :as config]))

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
    {:http-xhrio {:method :post
                  :uri (str config/api-url "/auth/login")
                  :response-format (ajax/json-response-format {:keywords? true})
                  :format (ajax/json-request-format)
                  :params body
                  :on-success [:login-success]
                  :on-failure [:login-failure]}})))

(re-frame/reg-event-fx
 :login-success
 [utils/common-interceptors]
 (fn [{:keys [db]} [_ response]]
  (let [token (:token response)
        refreshToken (:refreshToken response)
        firstName (:firstName response)
        lastName (:lastName response)
        {:keys [login]} (get-in db [:pages :landing :fields])
        {:keys [roles admin? id]} (utils/extract-user-roles token)]
      {:db (-> db
               (assoc-in [:users :current :id] id)
               (assoc-in [:users :current :token] token)
               (assoc-in [:users :current :refresh-token] refreshToken)
               (assoc-in [:users :current :roles] roles)
               (assoc-in [:users :current :email] login)
               (assoc-in [:users :current :firstName] firstName)
               (assoc-in [:users :current :lastName] lastName)
               (assoc-in [:users :current :logged?] true))
       :router {:handler (if admin? :users :flowers)}
       :dispatch (when admin? [:get-all-users/request])
       :dispatch-n [[:store-tokens {:token token
                                    :refresh-token refreshToken
                                    :id id
                                    :email login}]]})))

(re-frame/reg-event-fx
 :login-failure
 [utils/common-interceptors]
 (fn [_ [_ v]]
  {:dispatch [:app/show-message (-> v :response :message)]}))
