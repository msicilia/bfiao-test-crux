(ns bfiao-test-crux.core-test
  (:require [clojure.test :refer :all]
            [bfiao-test-crux.core :refer :all]
            [crux.api :as crux]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.set :as set]))


(defn start-db 
   "Opens a RocksDB-based Crux instance." 
 []
 (crux/start-node {:crux/document-store {:kv-store {:crux/module 'crux.rocksdb/->kv-store
                                                             :db-dir (io/file "./test-data")
                                                             :sync? true}}
                            :crux/index-store {:kv-store {:crux/module 'crux.rocksdb/->kv-store
                                                             :db-dir (io/file "./test-indexes")
                                                             :sync? true}}
                            :crux/tx-log {:kv-store {:crux/module 'crux.rocksdb/->kv-store
                                                     :db-dir (io/file "./test-logs")
                                                     :sync? true}}})    
)

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data) ;; First row is the header
            (map keyword) ;; Drop if you want string keys instead
            repeat)
       (rest csv-data)))



(defn stop-db [db]
    (.close db)
)




(defn fill-db [db]
  (def aux
    (csv-data->maps (csv/read-csv 
                     (io/reader "./test-fill-data/nodes.csv") 
                     :separator \; ))
  )
  (print (map 
            #(set/rename-keys % {:ID :crux.db/id} ) ; Include the Crux identifier of the document.
          aux)
  )
)

(defn empty-db 
  [db]
  (quote "Use crux.tx/evict with all the ids..."))

(defn with-db [f]
  (def testdb (start-db))
  (f)
  (stop-db testdb))

(use-fixtures :once with-db fill-db)

(deftest a-test
  (testing "I do nothing."
    (is (= 1 1))))

(deftest another-test
  (testing "I do nothing."
    (is (= 1 1))))