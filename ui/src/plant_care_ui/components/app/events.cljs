(ns plant-care-ui.components.app.events
  (:require [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :refer [common-interceptors]]))

(defn app-toggle-drawer [db [_ _]]
 (update-in db [:global :drawer :open?] not))

(re-frame/reg-event-db
 :app/toggle-drawer
 [common-interceptors]
 app-toggle-drawer)

(defn show-message [db [_ message]]
  (println "show message" message)
  (assoc-in db [:global :snackbar :message] message))

(re-frame/reg-event-db
  :app/show-message
  [common-interceptors]
  show-message)

(defn hide-snackbar [db [_ _]]
  (assoc-in db [:global :snackbar :message] nil))

(re-frame/reg-event-db
  :app/hide-snackbar
  [common-interceptors]
  hide-snackbar)
