(ns circleci.organization
  (:require [circleci.http :refer [defreq]]))

(defreq builds
  "GET /organization/:name - Return recent builds for an organization.

  * name"
  [:get [:organization name]])
