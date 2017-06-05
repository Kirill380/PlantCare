(ns plant-care-ui.router.core
  (:require [bide.core :as r]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljs-react-material-ui.reagent :as ui]
            [plant-care-ui.components.app.views :refer [app]]
            [plant-care-ui.router.nav :as nav]
            [plant-care-ui.pages.landing.views :refer [landing-page]]
            [plant-care-ui.pages.registration.views :refer [registration-page]]
            [plant-care-ui.pages.users.views :refer [users-page]]
            [plant-care-ui.router.subs]
            [plant-care-ui.router.events]))

(defn on-navigate [name params query]
  (re-frame/dispatch [:set-route {:handler name
                                  :params params
                                  :query query}]))


(r/start! nav/*router {:default :landing
                       :on-navigate on-navigate})

(defn router []
  (let [{:keys [handler params query]} @(re-frame/subscribe [:route])]
    [ui/mui-theme-provider
     [app
      (case handler
       :landing [landing-page]
       :registration [registration-page]
       :page1-by-id [:div (str "PAGE 1 " params)]
       :users [users-page]
       [:div "NOT FOUND"])]]))
