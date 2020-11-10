(ns bfiao-test-crux.core
  (:gen-class)
  (:require [crux.api :as crux])
  (:require [clojure.java.io :as io]))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def db (crux/start-node {:crux/document-store {:kv-store {:crux/module 'crux.rocksdb/->kv-store
                                                             :db-dir (io/file "./data")
                                                             :sync? true}}
                            :crux/index-store {:kv-store {:crux/module 'crux.rocksdb/->kv-store
                                                             :db-dir (io/file "./indexes")
                                                             :sync? true}}
                            :crux/tx-log {:kv-store {:crux/module 'crux.rocksdb/->kv-store
                                                     :db-dir (io/file "./logs")
                                                     :sync? true}}}
                            
                            ))
                            
  (println "BFiaO prototype...")
  (def example-node
    {:crux.db/id :node
     :name "Puerto de Barcelona"
     :cesar/type "Critical node"
     :position [41.3462356 2.133345]})
  ;;(crux/await-tx db (crux/submit-tx db [[:crux.tx/put example-node]]))
  ;;(print   (crux/entity-history (crux/db db) :node :asc))
  (print (crux/entity (crux/db db) :node))
  (.close db)
)


#_(defn -main
    ""
   [& args]
   ()
  )
