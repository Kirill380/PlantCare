(ns plant-care-ui.router.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-db :set-route
                 [(rf/path :route)]
                 (fn [db [_ new]]
                  new))
