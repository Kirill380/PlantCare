(ns plant-care-ui.pages.landing
  (:require [rum.core :as rum]
            [plant-care-ui.components.material :as m]))

(rum/defc landing-page []
  [:div {:style {:display "flex"
                 :flex-direction "column"
                 :justify-content "center"
                 :align-items "center"
                 :height 500
                 :width "100%"}}
   "Landing page stub"
   (m/button {:label "Show Alert"
              :onClick #(js/alert "ok. look here now!")})])
