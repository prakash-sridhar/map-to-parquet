(ns map-to-parquet.core
  (:gen-class)

  [:import
   [org.apache.spark.sql.types DataTypes]])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


(def type-map {java.lang.Long DataTypes/LongType
               java.lang.String DataTypes/StringType
               java.math.BigDecimal (DataTypes/createDecimalType 11 2)
               })

(defn dash-to-underscore
  [value]
  (clojure.string/replace value "-" "_"))

(defn identify-data-types-of-a-collection
  [collection-data]
  (reduce (fn [datatype-set value]
            (conj datatype-set (get type-map (type value))))
          #{} collection-data))

(defn create-data-structure
  [complex-data]

  (if (map? complex-data)
    (DataTypes/createStructType (reduce (fn [final-vect key-value]
              (conj final-vect (DataTypes/createStructField (dash-to-underscore (name key-value))
                                                  (if (or (map? (get complex-data key-value))
                                                          (vector? (get complex-data key-value))
                                                          (seq? (get complex-data key-value)))
                                                    (create-data-structure (get complex-data key-value))
                                                    (get type-map (type (get complex-data key-value))))
                                                  true)))
            [] (keys complex-data)))
    (if (or (vector? complex-data) (seq? complex-data))
      (if (and (= 1 (count complex-data)))
        (create-data-structure (first complex-data))
        (if (= 1 (count (identify-data-types-of-a-collection complex-data)))
          (DataTypes/createStructType (first (identify-data-types-of-a-collection complex-data)) true)
          ))
      ))

  )

(type {})
;(create-data-structure [{:first-name "xxx" :age 00 :salary 00.00M
 ;                       :address {:address-line1 "xxxx" :address-line2 "yyy" }
 ;                        :months-worked ["Jan" "Feb" "March" "April"]}])




(defn create-stringtype-map-by-default
  [map-val]
  (reduce (fn create-string-type
            [final-map key-value]
            (conj final-map [key-value DataTypes/StringType]))
          {} (keys map-val)))




