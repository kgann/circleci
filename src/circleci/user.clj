(ns circleci.user
  (:require [circleci.http :refer [defreq]]))

(defreq me
  "GET /me - Return user account details."
  [:get [:me]])

(defreq ssh-key
  "POST /user/ssh-key - Add a CircleCI key to your Github User account."
  [:post [:user :ssh-key]])

(defreq heroku-key
  "POST /user/heroku-key - Add your Heroku API key to CircleCI. Requires `:apikey` query param.

   ```
   (heroku-key :apikey ...)
  ```"
  [:post [:user :heroku-key]])
