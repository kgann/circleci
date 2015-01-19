(ns circleci.core-test
  (:require [clojure.test :refer :all]
            [circleci.core :refer [with-token configure!]]
            [circleci.http :refer [*token*]]))

(use-fixtures :each
  (fn [f]
    (alter-var-root #'circleci.http/*token* (constantly nil))
    (f)
    (alter-var-root #'circleci.http/*token* (constantly nil))))

(deftest token-binding
  (testing "with-token"
    (with-token "circle token"
      (is (= "circle token" *token*))))

  (testing "configure!"
    (configure! "123")
    (is (= "123" *token*))))
