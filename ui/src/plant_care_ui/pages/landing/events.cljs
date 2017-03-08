(ns plant-care-ui.pages.landing.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]))

(re-frame/reg-event-fx
 :login-request
 []
 (fn [coefx event]
   (println coefx event)
   {:http-xhrio {:method :get
                 :uri "https://google.com"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:login-success]
                 :on-failure [:login-failure]}}))

(re-frame/reg-event-fx
 :login-success
 []
 (fn [coefx event]
   (println "SUCCESS" coefx event)))

(re-frame/reg-event-fx
 :login-failure
 []
 (fn [coefx event]
   (println "FAIL" coefx event)))
