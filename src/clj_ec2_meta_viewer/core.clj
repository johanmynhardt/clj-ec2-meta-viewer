(ns clj-ec2-meta-viewer.core
  (:gen-class)
  (:require [ring.server.standalone :as rs]))

(def server
  (atom nil))

(def config
  (atom {:aws {:metadata/base "http://169.254.169.254/latest/meta-data"}
         :jetty {:port 80 :open-browser? false}}))

(defn simple-meta-proxy-handler [req]
  (let [{{:metadata/keys [base]} :aws} @config
        proxy-response (slurp (str base (:uri req)))]
    {:code 200
     :body (str proxy-response)}))

(defn -main [& args]
  (let [jetty (-> @config :jetty)]
    (println "Starting with jetty config" jetty)
    (reset! server (rs/serve simple-meta-proxy-handler jetty))))

(comment
  (swap!
   config
   (fn [conf]
     (-> conf
         (assoc-in [:jetty :port] 8080)
         (assoc-in [:aws :metadata/base] "http://example.com"))))
  
  (-main)
  
  (.stop @server))