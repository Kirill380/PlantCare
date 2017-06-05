(ns plant-care-ui.core
  (:require [cljsjs.material-ui]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.config]
            [plant-care-ui.router.core :refer [router]]
            [plant-care-ui.db.events]
            [re-frisk.core :refer [enable-re-frisk!]]))

(defn ^:export main []
  (re-frame/dispatch-sync [:init-db])
  (enable-re-frisk!)
  (reagent/render [router]
   (.getElementById js/document "app")))

(main)
