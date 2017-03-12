(ns plant-care-ui.pages.landing.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [plant-care-ui.utils.core :refer [common-interceptors]]))

(re-frame/reg-event-fx
 :login-request
 [common-interceptors]
 (fn [coefx event]
   {:http-xhrio {:method :get
                 :uri "https://google.com"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:login-success]
                 :on-failure [:login-failure]}}))

(re-frame/reg-event-fx
 :login-success
 [common-interceptors]
 (fn [coefx event]
   (println "SUCCESS" coefx event)))

(re-frame/reg-event-fx
 :login-failure
 [common-interceptors]
 (fn [coefx event]
   (println "FAIL" coefx event)))
