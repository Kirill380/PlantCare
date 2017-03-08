(ns plant-care-ui.router.core
  (:require [bide.core :as r]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.router.subs]
            [plant-care-ui.router.events]
            [plant-care-ui.components.material :as m]
            [plant-care-ui.pages.landing.core :refer [landing-page]]
            [plant-care-ui.pages.registration.core :refer [registration-page]]))

(def *router
  (r/router [["/" :landing]
             ["/registration" :registration]
             ["/page1/:id" :page1-by-id]]))

(defn on-navigate [name params query]
  (re-frame/dispatch [:set-route {:handler name
                                  :params params
                                  :query query}]))

(r/start! *router {:default :landing
                   :on-navigate on-navigate})

(def navigate! (partial r/navigate! *router))

(defn router []
  (let [{:keys [handler params query]} @(re-frame/subscribe [:route])]
    [m/mui-theme-provider
     (case handler
       :landing [landing-page]
       :registration [registration-page]
       :page1-by-id [:div (str "PAGE 1 " params)]
       [:div "NOT FOUND"])]))
