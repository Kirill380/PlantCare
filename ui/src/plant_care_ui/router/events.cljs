(ns plant-care-ui.router.events
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-event-db :set-route
                 [(re-frame/path :route)]
                 (fn [db [_ new]]
                  new))
