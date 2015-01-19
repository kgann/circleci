# CircleCI

A Clojure library for interacting with the [CircleCI REST API](https://circleci.com/docs/api)

See the generated API documentation for all available namespaces and their fn's.

## Documentaion
* [API docs](http://kgann.github.io/circleci)
* [Changelog](#changelog)

## Usage

```clojure
(require '[circleci.project :as p])
(require '[circleci.core :as ci])

;; your CircleCI API token
(def token "...")

(def username "...")
(def project "...")

;; block until request completion
;; return JSON parsed response body
(p/build username project :token token)

;; return immediately
;; execute callback upon request completion with JSON parsed body
(p/build username project :token token :callback (fn [body] ... ))

(def c (clojure.core.async/chan))

;; return immediately
;; upon request completion put! JSON parsed body on core.async channel
(p/build username project :token token :chan c)

;; specify http-kit client options
;; http://www.http-kit.org/client.html
(p/build username project :token token :http {:timeout 100})

;; specify additional query parameters as keyword arguments
(p/build username project :token token :param1 "one" :param2 2 :param3 :three)

;; inspect request/response options attached to response body
(def body (p/build ...))

(ci/details body)
=> {:opts {:method :get
           :url "..."
           :query-string { ... }}
           :status 200
           :headers { ... }}
```


Optionally, you may omit `:token ...` from each request call if you either:

```clojure
;; configure the token globally
(ci/configure! "your token")

;; supply a temporary binding
(ci/with-token "your token"
  (p/build ...))
```

## Changelog

* v0.1.0 - intial release

## License

Copyright © 2015 Kyle Gann

Distributed under the Eclipse Public License, the same as Clojure.
