(ns plant-care-ui.db.core
  (:require [re-frame.core :as re-frame]))

(def default-values
  {:pages
   {:registration {:fields
                          {:first-name ""
                           :last-name ""
                           :email ""
                           :password ""
                           :confirm-password ""}}
    :landing {:fields {:login ""
                       :password ""}}}})
