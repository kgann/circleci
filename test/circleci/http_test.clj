(ns circleci.http-test
  (:require [clojure.test :refer :all]
            [circleci.http :refer :all]
            [clojure.core.async :as async]
            [circleci.core :refer [details]]
            [org.httpkit.fake :refer [with-fake-http]]))

(defreq get-test
  [:get [:foo]])

(defreq post-test
  [:post [:foo]])

(defreq arg-test
  [:get [:foo arg1 :bar arg2 :baz arg3]])

(deftest pre-assertions
  (testing "token"
    (is (thrown-with-msg? AssertionError #"token" (get-test))))

  (testing "XOR chan callback"
    (is (thrown-with-msg? AssertionError #"chan"
                          (get-test :token "token" :chan "chan" :callback identity)))))

(deftest http-request
  (with-fake-http [(constantly true) "{}"]
    (testing "details"
      (is (= {:opts {:method :get
                     :url "https://circleci.com/api/v1/foo"
                     :query-params {:circle-token "123" :param "bar"}
                     :headers {"Accept" "application/json"}}
             :status 200
             :headers {:server "org.httpkit.fake"
                       :content-type "text/html"}}
             (details (get-test :token "123" :param "bar")))))

    (testing "GET"
      (let [resp (details (get-test :token "123" :param "bar"))]
        (testing "url"
          (is (= "https://circleci.com/api/v1/foo" (get-in resp [:opts :url]))))

        (testing "method"
          (is (= :get (get-in resp [:opts :method]))))

        (testing "query-params"
          (is (= {:circle-token "123" :param "bar"} (get-in resp [:opts :query-params]))))))

    (testing "POST"
      (let [resp (details (post-test :token "123" :param "bar"))]
        (testing "url"
          (is (= "https://circleci.com/api/v1/foo" (get-in resp [:opts :url]))))

        (testing "method"
          (is (= :post (get-in resp [:opts :method]))))

        (testing "query-params"
          (is (= {:circle-token "123"} (get-in resp [:opts :query-params]))))

        (testing "JSON serialized body"
          (is (= "{\"param\":\"bar\"}" (get-in resp [:opts :body]))))))))

(deftest http-kit-options
  (with-fake-http [(constantly true) "{}"]
    (let [resp (details (get-test :token "123" :http {:timeout 100}))]
      (testing "setting http-kit options"
        (is (= 100 (get-in resp [:opts :timeout])))))))

(deftest reserved-keyword-args
  (with-fake-http [(constantly true) "{}"]
    (let [resp (details (get-test :token "123" ::chan "chan"))]
      (testing "namespaced keywords allow for query-string kvs"
        (is (= {:chan "chan" :circle-token "123"} (get-in resp [:opts :query-params])))))))

(deftest request-fn-arguments
  (with-fake-http [(constantly true) "{}"]
    (testing "arglists"
      (is (= '([arg1 arg2 arg3 & options]) (:arglists (meta #'arg-test)))))

    (testing "type coercion"
      (is (= "https://circleci.com/api/v1/foo/1/bar/two/baz/three"
             (get-in (details (arg-test 1 :two "three" :token "123")) [:opts :url]))))))

(deftest callbacks
  (with-fake-http [(constantly true) "{\"callback\":\"executed\"}"]
    (let [state (atom nil)]
      (testing "callback"
        @(get-test :token "123" :callback (fn [body] (reset! state body)))
        (is (= {:callback "executed"} @state))))))

(deftest channels
  (with-fake-http [(constantly true) "{\"channel\":\"received body\"}"]
    (let [c (async/chan)]
      (testing "core.async channel"
        (get-test :token "123" :chan c)
        (is (= {:channel "received body"} (async/<!! c)))))))
