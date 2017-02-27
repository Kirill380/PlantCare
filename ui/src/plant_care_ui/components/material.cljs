(ns plant-care-ui.components.material)

(defn create-element
  ([elem] (create-element elem {} nil))
  ([elem props] (create-element elem props nil))
  ([elem props children]
   (js/React.createElement elem (clj->js props) (clj->js children))))

(defn mui-theme-provider [children]
  (create-element
   js/MaterialUI.MuiThemeProvider
   {}
   children))

(defn button [props]
  (create-element
   js/MaterialUI.FlatButton
   props))
