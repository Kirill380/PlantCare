(ns plant-care-ui.router.nav
  (:require [bide.core :as r]
            [re-frame.core :as re-frame]))

(def *router
  (r/router [["/" :landing]
             ["/registration" :registration]
             ["/users" :users]
             ["/users/:id" :user-by-id]
             ["/flowers" :flowers]
             ["/flowers/:id" :plant-by-id]
             ["/sensors" :sensors]
             ["/sensors/:id" :sensor-by-id]
             ["/profile" :profile]
             ["/connections" :connections]]))

(def navigate! (partial r/navigate! *router))

(def interceptor
  (re-frame/->interceptor
   :id :router-interceptor
   :after (fn [context]
            (println "context" context)
            (when-let [router (get-in context [:effects :router])]
               (navigate!
                (:handler router)
                (:params router)
                (:query router)))
            context)))

(re-frame/reg-fx
 :router
 (fn [fx]
   (when-let [{:keys [handler query params]} fx]
     (navigate! handler params query))))
