(defproject plant-care-ui "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.542"]
                 [org.clojure/core.async "0.3.443"
                  :exclusions [org.clojure/tools.reader]]
                 [org.clojure/tools.nrepl "0.2.13"
                  :exclusions [org.clojure/clojure]]
                 [reagent "0.6.2"
                  :exclusions [cljsjs/react cljsjs/react-dom]]
                 [re-frame "0.9.3"]
                 [funcool/bide "1.5.0"]
                 [cljs-react-material-ui "0.2.44"]
                 [day8.re-frame/http-fx "0.1.3"]
                 [cljs-ajax "0.6.0"]]

  :npm {:dependencies []}

  :plugins [[lein-figwheel "0.5.9" :exclusions [[org.clojure/clojure]]]
            [lein-cljsbuild "1.1.5" :exclusions [[org.clojure/clojure]]]
            [lein-ancient "0.6.10"]
            [lein-npm "0.6.2"]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"
                                    "out"
                                    "node_modules"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]
                :figwheel {:on-jsload "plant-care-ui.core/render"}

                :compiler {:main plant-care-ui.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/app.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [dirac.runtime.preload devtools.preload]
                           :external-config {
                                             :devtools/config {
                                                               :features-to-install [:formatters :hints]
                                                               :fn-symbol "F"
                                                               :print-config-overrides true}}}}



               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/app.js"
                           :main plant-care-ui.core
                           :optimizations :simple
                           :pretty-print false
                           :pseudo-names false}}]}

  :figwheel {:server-port 3000
             :css-dirs ["resources/public/css"]}

  :profiles {:dev {:dependencies [[binaryage/dirac "1.2.8"
                                   :exclusions [org.clojure/tools.reader]]
                                  [binaryage/devtools "0.9.4"]
                                  [figwheel-sidecar "0.5.10"]
                                  [com.cemerick/piggieback "0.2.1"]]
                   :plugins [[lein-kibit "0.1.3"]
                             [lein-bikeshed "0.4.1" :exclusions [[org.clojure/tools.cli]]]]
                   :source-paths ["src" "dev"]
                   :repl-options {:port 8230
                                  :nrepl-middleware [dirac.nrepl/middleware]
                                  :init (do
                                          (require 'dirac.agent)
                                          (dirac.agent/boot!))}}}
  :aliases {"lint" ["do" ["kibit"] ["bikeshed"]]
            "build" ["do" ["clean"] ["cljsbuild" "once" "min"]]})
