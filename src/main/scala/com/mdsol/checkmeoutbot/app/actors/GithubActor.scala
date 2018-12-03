package com.mdsol.checkmeoutbot.app.actors

import akka.actor.Actor
import com.mdsol.checkmeoutbot.app.models.GithubModels.{AccessToken, AuthCode}
import com.mdsol.checkmeoutbot.app.services._
import github4s.free.domain.OAuthToken

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util.{Failure, Success}


class GithubActor extends Actor with GithubServices {


  override def receive: Receive = {

    case AuthCode(code) =>
      println("processing oauthcode")
      processAuthCode(code)
//    case OAuthToken(access_token, token_type, scope) =>
//      createWebhook(access_token)
    case "createWebhook" =>
      createWebhook("access_token" +
        "")

  }


  def processAuthCode(code: String): Unit = {

    getAuthCodeService(code).onComplete {
      case Success(x) => x match {
        case Right(oAuthToken) => {
          println(oAuthToken)
          self ! oAuthToken.result
        }
        case Left(error) => println(error.printStackTrace())
      }
      case Failure(e) => e.printStackTrace()
    }
  }


  def createWebhook(access_token: String): Unit = {
    createWebhookService(Option(access_token)).onComplete {
      case Success(value) => value match {
        case Right(userInfo) => println(userInfo)
        case Left(error) => error.printStackTrace()
      }
      case Failure(exception) => exception.printStackTrace()
    }
  }




  def processOAuthToken(accessToken: String): Unit = {
    getUserDetailsService(Option(accessToken)).onComplete {
      case Success(value) => value match {
        case Right(userInfo) => println(userInfo)
        case Left(error) => error.printStackTrace()
      }
      case Failure(exception) => exception.printStackTrace()
    }
  }


}
