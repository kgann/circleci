(ns circleci.core
  (:require circleci.http))

(defn details
  "Return response details associated with this response body.

  * status
  * headers
  * error
  * opts"
  [body]
  (:circleci.http/resp (meta body)))

(defmacro with-token
  "Evaluate body with circleci.http/*token* bound to `token`."
  [token & body]
  `(binding [circleci.http/*token* ~token]
     ~@body))

(defn configure!
  "Configure your CircleCI API token globally."
  [token]
  (alter-var-root #'circleci.http/*token* (constantly token)))
