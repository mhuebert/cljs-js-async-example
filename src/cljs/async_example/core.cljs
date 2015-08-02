(ns async-example.core
  ^:figwheel-always
  (:require [cljs.js :as cljs]
            [cljs.pprint :refer [pprint]]
            [goog.net.XhrIo :as xhr]))

(enable-console-print!)

(defn load-source [{:keys [_ macros path] :as x} cb]
  (let [path (str path "." (if macros "clj" "cljs"))]
    (xhr/send path (fn [e]
                     (let [source (-> e .-target .getResponseText)]
                       (prn (str "fetched " path))
                       (cb {:lang :clj
                            :source source}))))))

(def st (cljs/empty-state))

(cljs/eval st '(ns async-example.foo
                 (:require-macros [cljs.core.async.macros :refer [go]])
                 (:require [cljs.core.async :refer [put! chan <! >!]]))
           {:load load-source
            :eval cljs/js-eval}
           (fn [result]
             (pprint result)))