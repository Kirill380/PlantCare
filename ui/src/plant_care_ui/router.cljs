(ns plant-care-ui.router
  (:require [bide.core :as r]
            [rum.core :as rum]
            [plant-care-ui.components.material :as m]
            [plant-care-ui.pages.landing :refer [landing-page]]))

(defonce route (atom))

(def *router
  (r/router [["/" ::landing]
             ["/page1" ::page1]
             ["/page1/:id" ::page1-by-id]]))

(defn on-navigate [name params query]
  (reset! route {:handler name
                 :params params
                 :query query}))

(r/start! *router {:default ::root
                   :on-navigate on-navigate})

(rum/defc router < rum/reactive [route *state]
  (let [{:keys [handler params query]} (rum/react route)
         state (rum/react *state)]
    (m/mui-theme-provider
     (case handler
       ::landing (landing-page)
       ::page1 [:div "PAGE 1"]
       ::page1-by-id [:div (str "PAGE 1 " params)]))))
