package com.mdsol.checkmeoutbot.app.controllers
import akka.actor.Actor
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import com.mdsol.checkmeoutbot.utils.open.Open
import spray.json._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

class AuthenticateController {

  val authUrl = "https://github.com/login/oauth/authorize?scope=write:repo_hook&client_id=3344f92de1bb619d2cb8"
  val accessToken = "https://github.com/login/oauth/access_token"


  def authenticate() = {
    println("Authenticating ...")
    Open.start(authUrl)


  }

  def hasBeenAuthenticated() = {

  }

}
