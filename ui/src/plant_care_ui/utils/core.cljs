(ns plant-care-ui.utils.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [goog.crypt.base64 :as base64]
            [clojure.string :as string]))

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

(defn map-values [f hash-map]
  (reduce (fn [result [k v]]
            (assoc result k (f v)))
          {}
          hash-map))

(defn deep-merge
  "Recursively merges maps. If keys are not maps, the last value wins."
  [& vals]
  (if (every? map? vals)
    (apply merge-with deep-merge vals)
    (last vals)))

(defn format-date [date]
  (.replace date "T" " "))

(defn date-formatter [date]
  [:span (format-date date)])

(defn get-id-link-formatter [action]
  (fn [id]
    [:div {:style {:color "red"
                   :width "100%"
                   :height "100%"
                   :cursor "pointer"}
           :on-click #(re-frame/dispatch [action id])}
      id]))


; it's bad to use js/JSON not provided by coeffect
(defn parse-auth-token [token]
  (js->clj
   (.parse js/JSON
    (base64/decodeString
     (second
      (string/split token #"\."))))
   :keywordize-keys true))


(defn extract-user-roles [token]
  (let [roles (:roles (parse-auth-token token))
        admin? (boolean (some #{"admin"} roles))]
    {:roles roles
     :admin? admin?}))
