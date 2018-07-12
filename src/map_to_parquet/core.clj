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

(def map-val {:age 27 :first_name "Prakash" :salary 100.03M})
(def vect-of-maps
  [{:age 27 :first_name "Prakash" :salary 100.03M}
   {:age 32 :first_name "Siva" :salary 101.03M}
   {:age 40 :first_name "Venkat" :salary 105.03M}])

(def vect-of-maps1
  [{:age nil :first_name nil :salary 100.03M}
   {:age nil :first_name "Siva" :salary 101.03M}
   {:age 40 :first_name "Venkat" :salary 105.03M}])

(def vect-of-maps2
  [{:age nil :first_name nil :salary 100.03M}
   {:age nil :first_name "Siva" :salary 101.03M}
   {:age nil :first_name "Venkat" :salary 105.03M}])

(defn create-stringtype-map-by-default
  [map-val]
  (reduce (fn create-string-type
            [final-map key-value]
            (conj final-map [key-value DataTypes/StringType]))
          {} (keys map-val)))

(defn create-datatype-map
  [vect-of-maps]
  (let [column-count (count (get vect-of-maps 0)) ]
    (merge (create-stringtype-map-by-default (get vect-of-maps 0))
      (loop [final-map {}
           map-val1 vect-of-maps]
      (if (or (= (count final-map) column-count) (empty? map-val1))
        final-map
        (do (let [map-val (first map-val1)]
            (recur (reduce (fn [final-map map-key]
                             (when (not (nil? (get map-val map-key)))
                               (merge final-map {map-key (get type-map (type (get map-val map-key)))}))
                             ) final-map (keys map-val)) (rest map-val1)))))))))

(create-datatype-map vect-of-maps2)

;(empty? (rest [{:a 1}]))

;(if (and 1 (not-empty {:a 1})) true false)


(comment (conj {} {:a 1})

  (conj {:a 1 :b 2 :c 3} {:b 5 :d 6})
  (conj {:a 1 :b 2} [:a 3])

  (count {:a 1 :b 3})
  (keys (get vect-of-maps 0))

  (println type-map)
  (reduce (fn data-to-type
            [type-map map-val]
            (let []

              )
            (if (not (nil? (type x)))
              (conj type-map x )
              ))))

