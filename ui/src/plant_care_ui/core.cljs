(ns plant-care-ui.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljsjs.material-ui]
            [cljsjs.material-ui-svg-icons]
            [plant-care-ui.config]
            [plant-care-ui.router.core :refer [router]]
            [plant-care-ui.db.events]))

(defn render []
  (reagent/render [router]
   (.getElementById js/document "app")))

(re-frame/dispatch-sync [:init-db])
(render)
