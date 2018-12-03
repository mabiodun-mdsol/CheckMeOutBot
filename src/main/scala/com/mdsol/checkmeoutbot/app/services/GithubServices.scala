package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.config.CMOBConfig
import github4s.Github
import github4s.Github._
import github4s.GithubResponses.{GHIO, GHResponse}
import github4s.free.domain.{Config, OAuthToken, User, Webhook}
import github4s.jvm.Implicits._
import scalaj.http.HttpResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait GithubServices {

  val httpConfig = CMOBConfig.Http
  val githubConfig = CMOBConfig.gitApp


  def getAuthCodeService(code: String): Future[GHResponse[OAuthToken]] = {

    println("Github service class getauhcode service entry point")

    val getAccessToken = Github(None).auth.getAccessToken(
      githubConfig.clientId.get,
      githubConfig.clientSecret.get,
      code,
      "",
      ""
    )
    getAccessToken.execFuture[HttpResponse[String]]()
  }

  def createWebhookService(accessToken: Option[String]): Future[GHResponse[Webhook]] = {

    val config = Config(
      content_type = "json",
      insecure_ssl = "",
      url = "http://examples.com/webhooks11116"
    )

    val createWebhook = Github(accessToken).webhooks.createWebhooks(
      owner = "mabiodun-mdsol",
      repo = "testRepo",
      name = "web",
      active = true,
      events = Seq("*"),
      config
    )
    createWebhook.execFuture[HttpResponse[String]]()
  }

}
