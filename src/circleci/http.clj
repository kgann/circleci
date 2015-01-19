(ns ^:no-doc circleci.http
  (:require [clojure.core.async :as async]
            [clojure.tools.macro :refer [name-with-attributes]]
            [cheshire.core :refer [parse-string generate-string]]
            [org.httpkit.client :as http]))

(def ^:dynamic *token* nil)
(def ^:const reserved #{:http :chan :callback :token})
(def ^:const base-uri "https://circleci.com/api/v1/")

(defn- parse-body
  "Parse JSON response body and attach response details as metadata."
  [{:keys [body] :as resp}]
  (with-meta (parse-string body true) {::resp (dissoc resp :body)}))

(defn- segments->url
  "Given a collection of path segments, construct a valid URL"
  [segments]
  (let [segments (map #(if (keyword? %) (name %) (str %)) segments)]
    (apply str base-uri (interleave segments (repeat \/)))))

(defn- http-options
  "Generate http-kit options. POST requests will send the body as JSON."
  [method opts]
  (let [query (into {}
                (for [[k v] (apply dissoc opts reserved)]
                  [(-> k name keyword) v]))]
    (if (= :post method)
      (assoc (:http opts) :body (generate-string query))
      (assoc (:http opts) :query-string query))))

(defn make-request
  [method segments {:keys [chan callback token] :or {token *token*} :as opts}]
  {:pre [(not (and chan callback)) (some? token)]}
  (let [request (partial http/request
                         (-> (http-options method opts)
                             (assoc :url (segments->url segments) :method method)
                             (assoc-in [:query-string :circle-token] token)))]
    (cond
      chan (request #(async/put! chan (parse-body %)))
      callback (request (comp callback parse-body))
      :else (parse-body @(request)))))

(defmacro defreq
  [name & body]
  (let [[name [[method binding]]] (name-with-attributes name body)
        symbols (filter symbol? binding)]
    `(defn ~name [~@symbols & ~'options]
       (make-request ~method ~binding (apply hash-map ~'options)))))
