(ns plant-care-ui.router.nav
  (:require [bide.core :as r]
            [re-frame.core :as re-frame]))

(def *router
  (r/router [["/" :landing]
             ["/registration" :registration]
             ["/page1/:id" :page1-by-id]]))

(def navigate! (partial r/navigate! *router))

(def interceptor
  (re-frame/->interceptor
   :id :router-interceptor
   :after (fn [context]
            (when-let [router (get-in context [:effects :router])]
               (navigate!
                (:handler router)
                (:params router)
                (:query router)))
            context)))
