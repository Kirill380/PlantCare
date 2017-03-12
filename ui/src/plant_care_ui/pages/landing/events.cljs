(ns plant-care-ui.pages.landing.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [plant-care-ui.utils.core :as utils]))

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

(re-frame/reg-event-fx
 :login-request
 [utils/common-interceptors]
 (fn [coefx event]
   {:http-xhrio {:method :get
                 :uri "https://google.com"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:login-success]
                 :on-failure [:login-failure]}}))

(re-frame/reg-event-fx
 :login-success
 [utils/common-interceptors]
 (fn [coefx event]
   (println "SUCCESS" coefx event)))

(re-frame/reg-event-fx
 :login-failure
 [utils/common-interceptors]
 (fn [coefx event]
   (println "FAIL" coefx event)))
