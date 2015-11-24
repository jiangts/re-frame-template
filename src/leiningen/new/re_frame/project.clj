(defproject {{ns-name}} "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.145"]
                 [reagent "0.5.1"]
                 [re-frame "0.5.0"]{{#re-com?}}
                 [re-com "0.6.2"]{{/re-com?}}{{#routes?}}
                 [secretary "1.2.3"]{{/routes?}}{{#garden?}}
                 [garden "1.2.5"]{{/garden?}}{{#handler?}}
                 [compojure "1.4.0"]
                 [ring "1.4.0"]{{/handler?}}
                 [prismatic/schema "1.0.1"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.4.1" :exclusions [cider/cider-nrepl]]{{#garden?}}
            [lein-garden "0.2.6"]{{/garden?}}
            [lein-externs "0.1.5"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"{{#test?}}
                                    "test/js"{{/test?}}{{#garden?}}
                                    "resources/public/css/compiled"{{/garden?}}]

  :figwheel {:css-dirs ["resources/public/css"]{{#handler?}}
             :ring-handler {{name}}.handler/handler{{/handler?}}}

  {{#garden?}}
  :garden {:builds [{:id "screen"
                     :source-paths ["src/clj"]
                     :stylesheet {{name}}.css/screen
                     :compiler {:output-to "resources/public/css/compiled/screen.css"
                                :pretty-print? true}}]}

  {{/garden?}}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs" "src/cljc"]

                        :figwheel {:on-jsload "{{name}}.core/mount-root"}

                        :compiler {:main {{name}}.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {{#test?}}
                       {:id "test"
                        :source-paths ["src/cljs" "test/cljs"]
                        :notify-command ["phantomjs" "test/unit-test.js" "test/unit-test.html"]
                        :compiler {:optimizations :whitespace
                                   :pretty-print true
                                   :output-to "test/js/app_test.js"
                                   :warnings {:single-segment-namespace false}}}

                       {{/test?}}
                       {:id "min"
                        :source-paths ["src/cljs"]
                        :compiler {:main {{name}}.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :closure-defines {"goog.DEBUG" false}
                                   :externs ["externs/externs.js"]
                                   :pretty-print false}}]})

