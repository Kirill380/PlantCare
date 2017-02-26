(ns app.router
  (:require [bide.core :as r]
            [rum.core :as rum]))

(defonce route (atom))

(def *router
  (r/router [["/" ::root]
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
    [:div
     (case handler
       ::root [:div "ROOT"]
       ::page1 [:div "PAGE 1"]
       ::page1-by-id [:div (str "PAGE 1 " params)])]))
