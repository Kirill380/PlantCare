(ns plant-care-ui.db.core
  (:require [re-frame.core :as re-frame]))

(def default-values
  { :route {:handler :landing}
    :flowers {:all {}}
    :users {:current {:logged? false}}
    :pages {:registration {:fields
                            {:first-name {:value ""
                                          :error-message nil}
                             :last-name {:value ""
                                         :error-message nil}
                             :email {:value ""
                                     :error-message nil}
                             :password {:value ""
                                        :error-message nil}
                             :confirm-password {:value ""
                                                :error-message nil}}}}

     :landing {:fields {:login ""}
                       :password ""}})
