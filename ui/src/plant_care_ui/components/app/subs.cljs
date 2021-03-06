(ns plant-care-ui.components.app.subs
  (:require [re-frame.core :as re-frame]))

(defn app-drawer-open? [db _]
  (->> db
       :global
       :drawer
       :open?))

(re-frame/reg-sub
 :app/drawer-open?
  app-drawer-open?)

(defn app-snackbar [db _]
  (->> db
       :global
       :snackbar))

(re-frame/reg-sub
  :app/snackbar
  app-snackbar)
