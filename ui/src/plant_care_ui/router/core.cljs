(ns plant-care-ui.router.core
  (:require [bide.core :as r]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljs-react-material-ui.reagent :as ui]
            [plant-care-ui.components.app.views :refer [app loading-indicator]]
            [plant-care-ui.router.nav :as nav]
            [plant-care-ui.pages.landing.views :refer [landing-page]]
            [plant-care-ui.pages.registration.views :refer [registration-page]]
            [plant-care-ui.pages.users.views :refer [users-page user-by-id-page]]
            [plant-care-ui.pages.flowers.views :refer [flowers-page flower-by-id-page]]
            [plant-care-ui.pages.sensors.views :refer [sensors-page sensor-by-id-page]]
            [plant-care-ui.router.subs]
            [plant-care-ui.router.events]))

(defn on-navigate [name params query]
  (re-frame/dispatch [:set-route {:handler name
                                  :params params
                                  :query query}]))


(r/start! nav/*router {:default :landing
                       :on-navigate on-navigate})

(def routing
  {:landing {:render (fn [_ _] [landing-page])
             :available-for #{:no-roles "admin" "regularUser"}}

   :registration {:render (fn [_ _] [registration-page])
                  :available-for #{:no-roles "admin" "regularUser"}}

   :users {:render (fn [_ _] [users-page])
           :available-for #{"admin"}}

   :user-by-id {:render (fn [params _] [user-by-id-page (:id params)])
                :available-for #{"admin"}}

   :flowers {:render (fn [_ _] [flowers-page])
             :available-for #{"regularUser"}}

   :plant-by-id {:render (fn [params _] [flower-by-id-page (:id params)])
                 :available-for #{"regularUser"}}
   :sensors {:render (fn [_ _] [sensors-page])
             :available-for #{"regularUser"}}
   :sensor-by-id {:render (fn [{:keys [id]} _] [sensor-by-id-page id])
                  :available-for #{"regularUser"}}})

(defn router []
  (let [{:keys [handler params query]} @(re-frame/subscribe [:route])
        roles @(re-frame/subscribe [:current-user-roles])
        requested-route (get routing handler)
        available? (boolean (some (:available-for requested-route) roles))]

    (when-not available?
      (nav/navigate! :landing))

    (when-not requested-route
      [:div "NOT FOUND"])

    [ui/mui-theme-provider
     [app
      (if available?
        (apply (:render requested-route) [params query])
        [loading-indicator])]]))
