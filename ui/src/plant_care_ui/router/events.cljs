(ns plant-care-ui.router.events
  (:require [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :refer [common-interceptors]]))

(re-frame/reg-event-db :set-route
                 [(re-frame/path :route) common-interceptors]
                 (fn [db [_ new]]
                  new))
