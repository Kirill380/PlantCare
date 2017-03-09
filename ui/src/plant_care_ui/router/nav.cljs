(ns plant-care-ui.router.nav
  (:require [bide.core :as r]))

(def *router
  (r/router [["/" :landing]
             ["/registration" :registration]
             ["/page1/:id" :page1-by-id]]))

(def navigate! (partial r/navigate! *router))
