(ns plant-care-ui.utils.core
  (:require [re-frame.core :as re-frame]))

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
