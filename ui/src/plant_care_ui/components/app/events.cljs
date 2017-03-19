(ns plant-care-ui.components.app.events
  (:require [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :refer [common-interceptors]]))

(defn app-toggle-drawer [db [_ _]]
 (update-in db [:global :drawer :open?] not))

(re-frame/reg-event-db
 :app/toggle-drawer
 [common-interceptors]
 app-toggle-drawer)
