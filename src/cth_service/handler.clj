(ns cth-service.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [cheshire.core :as json]
            [postal.core :as postal]))

(def user "awsuser")
(def pass "awspass")
(def from "alex@extreme-nelsons.net")
(def to "sklevine407@gmail.com")
(def host "email-smtp.us-east-1.amazonaws.com")
(def port 587)

(defn send-email []
  "Sends an email via AWS SES"
  (postal/send-message {:user user :pass pass
                 :host host
                 :port port}
                {:from from :to to
                 :subject "Test from Amazon SES" :body "Test!!!11"}))

(defn post-transaction []
  "Posts a transaction to SES"
  (send-email)
  {:status 200
   :headers {"Content-Type" "application/json charset=utf-8"}
   :body (json/generate-string
           {:disposition "accepted"
            :status "pending"})})

(defroutes app-routes
  (GET "/" [] "Hello World")
           (GET "/transactions" [] (post-transaction))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
