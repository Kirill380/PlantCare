(ns plant-care-ui.pages.users.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [plant-care-ui.utils.core :refer [listen]]
            [plant-care-ui.pages.users.subs]
            [plant-care-ui.pages.users.events]))

(defn users-list []
  (let [users (listen :all-users-list)]
    [:ul
      (for [user users]
        [:li {:key (:email user)}
             (:email user)])]))


(defn users-page []
  [:div {:style {:display "flex"
                 :flex-direction "column"
                 :margin-left 25}}
   [:h2 "Users page"]
   [:p "For now this is just a list of users"]
   [:p "It will be replaced with a rich table with sorting / pagination with custom cell for remove button etc."]
   [:h3 "Users list:"]
   [users-list]])
