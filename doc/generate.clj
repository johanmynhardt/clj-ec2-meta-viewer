(ns generate
  (:require [codox.main]))

(defn -main [& args]
  (codox.main/generate-docs 
   {:name "clj-ec2-meta-viewer"
    :version "0.0.1"
    :description "A simple proxy that can be used to extract EC2 metadata from a running instance."
    
    :metadata {:doc/format :markdown}}))