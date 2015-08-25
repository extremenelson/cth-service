(ns cth-service.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as ring-json]
            [ring.util.response :as rr]
            [postal.core :as postal]
            [environ.core :refer [env]]))

(def ses-user
  (env :aws-ses-id))
(def ses-pass
  (env :aws-ses-pwd))
(def from "alex@extreme-nelsons.net")
(def to "sklevine407@gmail.com")
(def host "email-smtp.us-west-2.amazonaws.com")
(def port 587)

(defn send-email []
  "Sends an email via AWS SES"
  (postal/send-message {:user ses-user :pass ses-pass
                 :host host
                 :port port
                        :tls true}
                {:from from :to to
                 :subject "Test from Amazon SES" :body "Test!!!11"}))

(defn send-email2 []
  "Bogus "
  (println (System/getenv))
  (println ses-user)
  (println ses-pass)
  nil)

(defn post-transaction []
  "Posts a transaction to SES"
  (send-email)
  {:status 200
   :headers {"Content-Type" "application/json charset=utf-8"}
   :body {:disposition "accepted"
          :status "pending"}})

(defroutes app-routes
  (GET "/" [] "Hello World")
           (GET "/transactions" [] (rr/response (post-transaction)))
  (route/not-found "Not Found"))

(def app
  (-> app-routes (ring-json/wrap-json-response)
      (wrap-defaults site-defaults)))
