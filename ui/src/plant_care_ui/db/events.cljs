(ns plant-care-ui.db.events
  (:require [re-frame.core :as re-frame]
            [plant-care-ui.db.core :refer [default-values]]))

(re-frame/reg-event-fx
 :init-db
 (fn [{:keys [db]} _]
   {:db default-values}))
