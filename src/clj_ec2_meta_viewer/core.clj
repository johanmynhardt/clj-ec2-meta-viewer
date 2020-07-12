(ns clj-ec2-meta-viewer.core
  (:gen-class)
  (:require
   [clojure.pprint :as pprint]
   [clojure.tools.cli :as cli]
   [ring.server.standalone :as rs]))

(def ^{:private true} server
  (atom nil))

(def ^{:private true} config
  (atom {:aws {:metadata/base "http://169.254.169.254/latest/meta-data"}
         :jetty {:port 80 :open-browser? false}}))

(defn ^{:private true} simple-meta-proxy-handler
  [req]
  {:code 200
   :body (str (slurp (str (-> @config :aws :metadata/base) (:uri req))))})

(def options-spec
  "The defined options available when starting from the CLI."
  [["-b" "--base BASE" "Base URL onto which (:uri req) will be appended." :default (-> @config :aws :metadata/base)]
   ["-p" "--port PORT" "The port on which to start the service." :default (-> @config :jetty :port) :parse-fn #(Integer/parseInt %)]
   ["-h" nil :id :help :desc "Prints this help and exits."]])

(defn with-options
  "Return a map with the simple keys updated in the correct location of the `config` map."
  [{:keys [port base] :as options}]
  (-> @config
      (update-in [:jetty :port] (fn [o n] (or n o)) port)
      (update-in [:aws :metadata/base] (fn [o n] (or n o)) base)))

(defn status
  "Return a map with selected fields from a bean view onto `@server`."
  []
  (when @server
    (select-keys (bean @server) [:started :starting :state :running :stopping :stopped])))

(defn start
  "Start the server with the calculated config map. See [[with-options]]."
  [{:keys [jetty] :as conf}]
  (println "Starting with config:" \newline
           (with-out-str (pprint/pprint conf)))
  (reset! config conf)
  (reset! server
          (rs/serve simple-meta-proxy-handler jetty))
  (status))

(defn- stop []
  (when @server
    (println "Stopping...")
    (.stop @server))
  (status))

(defn restart [& [opts]]
  (stop)
  (start (or opts @config)))

(defn -main [& args]
  (let [{:keys [options summary errors]} (cli/parse-opts args options-spec)]
    (cond
      errors (println "Could not parse all options:" errors)
      (:help options) (println summary)
      :else (start (with-options options)))))

(comment
  (-main)
  
  (start (with-options {:port 8080}))
  (start (with-options {:port 8080 :base "http://example.org"}))
  @config
  (stop)
  (restart (with-options {:port 8090 :base "http://example.org"}))
  (status)
  )