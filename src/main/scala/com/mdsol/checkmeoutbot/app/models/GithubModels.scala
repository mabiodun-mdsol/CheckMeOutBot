package com.mdsol.checkmeoutbot.app.models

object GithubModels {

  case class Authenticate(client_id:String, client_secret:String, code:String)
  case class AuthCode(code: String)
  case class AccessToken(accessToken: String)

  object AccessToken {
    def apply(accessToken: String) = new AccessToken(accessToken)
  }
}
