(ns plant-care-ui.pages.flowers.subs
  (:require [re-frame.core :as re-frame]))


(re-frame/reg-sub
  :all-flowers
  (fn [db _]
    (->> db
         :flowers
         :all)))

(re-frame/reg-sub
  :all-flowers-list
  (fn [db _]
    (->> db
         :flowers
         :all
         vals)))

(re-frame/reg-sub
  :plant-by-id
  (fn [db [_ id]]
    (-> db
        :flowers
        :all
        (get (js/parseInt id)))))
