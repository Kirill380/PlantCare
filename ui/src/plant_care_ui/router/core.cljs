(ns plant-care-ui.router.core
  (:require [bide.core :as r]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
            [plant-care-ui.router.subs]
            [plant-care-ui.router.events]
            [plant-care-ui.components.material :as m]
            [plant-care-ui.pages.landing :refer [landing-page]]))

(def *router
  (r/router [["/" ::landing]
             ["/page1" ::page1]
             ["/page1/:id" ::page1-by-id]]))

(defn on-navigate [name params query]
  (rf/dispatch [:set-route {:handler name
                            :params params
                            :query query}]))

(r/start! *router {:default ::root
                   :on-navigate on-navigate})

(defn router []
  (let [{:keys [handler params query]} @(rf/subscribe [:route])]
    [m/mui-theme-provider
     (case handler
       ::landing (landing-page)
       ::page1 [:div "PAGE 1"]
       ::page1-by-id [:div (str "PAGE 1 " params)]
       [:div "NOT FOUND"])]))
