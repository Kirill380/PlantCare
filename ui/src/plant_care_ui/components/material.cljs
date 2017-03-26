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

(def app-bar
  (reagent/adapt-react-class js/MaterialUI.AppBar))

(def drawer
  (reagent/adapt-react-class js/MaterialUI.Drawer))

(def menu
  (reagent/adapt-react-class js/MaterialUI.Menu))

(def menu-item
  (reagent/adapt-react-class js/MaterialUI.MenuItem))

(def divider
  (reagent/adapt-react-class js/MaterialUI.Divider))

(def paper
  (reagent/adapt-react-class js/MaterialUI.Paper))

(defn dashboard-icon
  ([] (dashboard-icon #js {}))
  ([props] (reagent/create-element js/MaterialUISvgIcons.ActionDashboard props)))

(defn flower-icon
  ([] (flower-icon #js {}))
  ([props] (reagent/create-element js/MaterialUISvgIcons.MapsLocalFlorist props)))

(defn ethernet-icon
  ([] (ethernet-icon #js {}))
  ([props] (reagent/create-element js/MaterialUISvgIcons.ActionSettingsEthernet props)))

(defn memory-icon
  ([] (memory-icon #js {}))
  ([props] (reagent/create-element js/MaterialUISvgIcons.HardwareMemory props)))
