(ns plant-care-ui.router.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub :route
            (fn [db _]
              (:route db)))
