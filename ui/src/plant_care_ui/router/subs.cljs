(ns plant-care-ui.router.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub :route
            (fn [db _]
              (:route db)))
