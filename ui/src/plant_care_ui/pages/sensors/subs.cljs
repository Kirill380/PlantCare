(ns plant-care-ui.pages.sensors.subs
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]))
            ; [plant-care-ui.utils.core :refer [listen build-text-field-options]]

(re-frame/reg-sub
 :all-sensors
 (fn [db]
   (->> db
        :sensors
        :all)))

(re-frame/reg-sub
  :all-sensors-list
  (fn [db]
    (->> db
         :sensors
         :all
         vals)))

(re-frame/reg-sub
 :sensor-by-id
 (fn [db [_ id]]
   (->> db
        :sensors
        :all
        (get (js/parseInt id)))))

(re-frame/reg-sub
 :last-created-firmware
 (fn [db]
   (->> db
        :firmwares
        :last-created)))
