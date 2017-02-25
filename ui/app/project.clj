(defproject app "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async "0.2.395"
                  :exclusions [org.clojure/tools.reader]]
                 [rum "0.10.8"]]

  :plugins [[lein-figwheel "0.5.9"]
            [lein-cljsbuild "1.1.5" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]
                :figwheel {:on-jsload "app.core/on-js-reload"}

                :compiler {:main app.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/app.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [dirac.runtime.preload]}}
               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/app.js"
                           :main app.core
                           :optimizations :advanced
                           :pretty-print false}}]}

  :figwheel {:server-port 3000
             :css-dirs ["resources/public/css"]}

  :profiles {:dev {:dependencies [[binaryage/dirac "1.1.5"]
                                  [figwheel-sidecar "0.5.9"]
                                  [com.cemerick/piggieback "0.2.1"]]
                   :plugins [[lein-kibit "0.1.3"]
                             [lein-bikeshed "0.4.1"]]
                   :source-paths ["src" "dev"]
                   :repl-options {:port 8230
                                  :nrepl-middleware [dirac.nrepl/middleware]
                                  :init (do
                                          (require 'dirac.agent)
                                          (dirac.agent/boot!))}}}
  :aliases {"lint" ["do" ["kibit"] ["bikeshed"]]})
