(ns circleci.build
  (:require [circleci.http :refer [defreq]]))

(defreq recent
  "GET /recent-builds - Return summary for the 30 most recent builds."
  [:get [:recent-builds]])

(defreq details
  "GET /project/:username/:project/:build - Return build details.

  * username
  * project
  * build"
  [:get [:project username project build]])

(defreq retry
  "POST /project/:username/:project/:build/retry - Retry a specific build.

  * username
  * project
  * build"
  [:post [:project username project build :retry]])

(defreq cancel
  "POST /project/:username/:project/:build/cancel - Cancel a specific build.

  * username
  * project
  * build"
  [:post [:project username project build :cancel]])

(defreq artifacts
  "GET /project/:username/:project/:build/artifacts - Return artifacts from specific build.

  * username
  * project
  * build"
  [:get [:project username project build :artifacts]])
