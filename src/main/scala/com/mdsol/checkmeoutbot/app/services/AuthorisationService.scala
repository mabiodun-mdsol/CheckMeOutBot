package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.config.CMOBConfig
import com.mdsol.checkmeoutbot.config.CMOBConfig.gitApp.GITHUB_ACCESS_TOKEN
import github4s.Github
import github4s.Github._
import github4s.GithubResponses.GHResponse
import github4s.free.domain._
import github4s.jvm.Implicits._
import org.slf4j.LoggerFactory
import scalaj.http.HttpResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

trait AuthorisationService {

  private val log = LoggerFactory.getLogger(this.getClass)


  val githubConfig = CMOBConfig.gitApp

  def authorisationHandler(): Unit = {

    getAccessToken().onComplete {
      case Success(x) => x match {
        case Right(oAuthToken) => {
          GITHUB_ACCESS_TOKEN = Some(oAuthToken.result.token)
          log.info("ACCESS TOKEN: " + GITHUB_ACCESS_TOKEN.get)
        }
        case Left(error) => log.error("Couldn't get github access token", error.printStackTrace())
      }
      case Failure(e) => e.printStackTrace()
    }
  }

  def authCodeHandler(code: String) {
    getAuthCodeService(code).onComplete {
      case Success(x) => x match {
        case Right(oAuthToken) => {
          GITHUB_ACCESS_TOKEN = Some(oAuthToken.result.access_token)
          log.info("ACCESS TOKEN: " + oAuthToken.result.access_token)
        }
        case Left(error) => println(error.printStackTrace())
      }
      case Failure(e) => e.printStackTrace()
    }
  }


  def getAccessToken() = {

    val res = Github(None).auth.newAuth(
      githubConfig.USERNAME.get,
      githubConfig.PASSWORD.get,
      List("admin:repo_hook"),
      "",
      githubConfig.CLIENT_ID.get,
      githubConfig.CLIENT_SECRET.get
    )
    res.execFuture[HttpResponse[String]]()
  }


  def getAuthCodeService(code: String): Future[GHResponse[OAuthToken]] = {
    println("Github service class getauhcode service entry point")
    val getAccessToken = Github(None).auth.getAccessToken(
      githubConfig.CLIENT_ID.get,
      githubConfig.CLIENT_SECRET.get,
      code,
      "",
      ""
    )
    getAccessToken.execFuture[HttpResponse[String]]()
  }


}