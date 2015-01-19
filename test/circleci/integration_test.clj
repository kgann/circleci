(ns circleci.integration-test
  (:require [clojure.test :refer :all]
            [circleci.core :refer [details]]
            [circleci.organization :as org]))

(defonce token (System/getenv "CIRCLECI_TOKEN"))
(defonce org (System/getenv "CIRCLECI_ORG"))

(use-fixtures :each
  (fn [f]
    (if (and (some? token) (some? org))
      (f)
      (println "Integration tests have been skipped. See README."))))

(deftest integration
  (is (= 200 (-> (org/builds org :token token)
                 (details)
                 (:status)))))
