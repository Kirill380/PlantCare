(ns plant-care-ui.utils.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]))

(def common-interceptors
  [(when ^boolean goog.DEBUG re-frame/debug)])

(defn listen [key]
  @(re-frame/subscribe [key]))

(defn build-text-field-options [label dispatch-key value]
  {:floating-label-text label
   :value value
   :on-change #(re-frame/dispatch
                [dispatch-key
                 (-> % .-target .-value)])})

(defn wrap-with-react-hoc [hoc & params]
  (let [wrapper (apply hoc params)] ; apply is not tested :)
    (fn [reagent-component]
      (reagent/adapt-react-class
       (wrapper
        (reagent/reactify-component reagent-component))))))

(def mui-themeable
  (wrap-with-react-hoc js/MaterialUIStyles.muiThemeable))
