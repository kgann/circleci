(ns circleci.project
  (:require [circleci.http :refer [defreq]]))

(defreq all
  "GET /projects - Return all projects."
  [:get [:projects]])

(defreq build
  "POST /project/:username/:project - Build latest commit for project.

  * username
  * project"
  [:post [:project username project]])

(defreq recent-builds
  "GET /project/:username/:project - Return recent builds for project.

  * username
  * project"
  [:get [:project username project]])

(defreq recent-branch-builds
  "GET /project/:username/:project/tree/:branch - Return recent builds for project branch.

  * username
  * project
  * branch"
  [:get [:project username project :tree branch]])

(defreq build-branch
  "POST /project/:username/:project/tree/:branch - Build latest commit for project branch.

  * username
  * project
  * branch"
  [:post [:project username project :tree branch]])

(defreq clear-cache
  "DELETE /project/:username/:project/build-cache - Delete project build cache.

  * username
  * project"
  [:delete [:project username project :build-cache]])

(defreq enable
  "POST /project/:username/:project/enable - Enable a project.

  * username
  * project"
  [:post [:project username project :enable]])

(defreq follow
  "POST /project/:username/:project/follow - Follow a project.

  * username
  * project"
  [:post [:project username project :follow]])

(defreq unfollow
  "POST /project/:username/:project/unfollow - Unfollow a project.

  * username
  * project"
  [:post [:project username project :unfollow]])

(defreq settings
  "GET /project/:username/:project/settings - Return project settings.

  * username
  * project"
  [:get [:project username project :settings]])
