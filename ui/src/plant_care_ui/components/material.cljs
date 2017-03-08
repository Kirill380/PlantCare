(ns plant-care-ui.components.material
  (:require [reagent.core :as reagent]))

(defn create-element
  ([elem] (create-element elem {} nil))
  ([elem props] (create-element elem props nil))
  ([elem props children]
   (js/React.createElement elem (clj->js props) (clj->js children))))

(def mui-theme-provider
  (reagent/adapt-react-class js/MaterialUI.MuiThemeProvider))

(defn button [props]
  (create-element
   js/MaterialUI.FlatButton
   props))
