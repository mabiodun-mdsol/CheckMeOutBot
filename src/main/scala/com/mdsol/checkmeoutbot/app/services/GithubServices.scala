package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.app.actors._
import com.mdsol.checkmeoutbot.app.models.GithubModels.AccessToken
import com.mdsol.checkmeoutbot.config.CMOBConfig
import github4s.{Github, GithubResponses}
import github4s.Github._
import github4s.GithubResponses.{GHResponse, GHResult}
import github4s.free.domain.{OAuthToken, User}
import github4s.jvm.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scalaj.http.HttpResponse

import scala.concurrent.Future

trait GithubServices {

  val httpConfig = CMOBConfig.Http
  val githubConfig = CMOBConfig.gitApp




  def getAuthCodeService(code: String): Future[GHResponse[OAuthToken]] = {



    val getAccessToken = Github(None).auth.getAccessToken(
      githubConfig.clientId.get,
      githubConfig.clientSecret.get,
      code,
      "",
      ""
    )
    getAccessToken.execFuture[HttpResponse[String]]()
  }


  def getUserDetailsService(accessToken: Option[String]): Future[GHResponse[User]] = {
    val getAuth = Github(accessToken).users.getAuth
    getAuth.execFuture[HttpResponse[String]]()

  }

}


