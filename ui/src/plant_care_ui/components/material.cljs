(ns plant-care-ui.components.material
  (:require [reagent.core :as reagent]))

(def mui-theme-provider
  (reagent/adapt-react-class js/MaterialUI.MuiThemeProvider))

(def flat-button
  (reagent/adapt-react-class js/MaterialUI.FlatButton))

(def raised-button
  (reagent/adapt-react-class js/MaterialUI.RaisedButton))

(def text-field
  (reagent/adapt-react-class js/MaterialUI.TextField))
