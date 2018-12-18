package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.app.models.GithubModels.PullRequestEvent
import com.mdsol.checkmeoutbot.config.CMOBConfig
import com.mdsol.checkmeoutbot.core.PullRequestLogic
import com.mdsol.checkmeoutbot.repository.GithubRepos
import com.mdsol.checkmeoutbot.utils.GithubAuthorisationConstantUtils.WEBHOOK_ENDPOINT
import github4s.Github
import github4s.Github._
import github4s.GithubResponses.GHResponse
import github4s.free.domain.{Config, OAuthToken, Webhook}
import github4s.jvm.Implicits._
import org.slf4j.LoggerFactory
import scalaj.http.HttpResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait GithubServices {

  private val log = LoggerFactory.getLogger(this.getClass)

  val githubConfig = CMOBConfig.gitApp

  def getAccessToken() = {
    val res = Github(None).auth.newAuth(
      "mabiodun-mdsol",
      "Rosemary!2",
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


  def isWebhookCreated(accessToken: Option[String], owner: String, repo: String): Boolean = {

    log.info(s"Checking if webhook is created for:" + owner + repo)

    val listWebhooks = Github(accessToken).webhooks.listWebhooks(
      owner = owner,
      repo = repo
    )

    listWebhooks.exec[cats.Id, HttpResponse[String]]() match {
      case Right(webhooks) =>
        println(webhooks)
        webhooks.result.exists(x => x.config.url.equals(WEBHOOK_ENDPOINT))
      case Left(error) =>
        log.error("Couldn't get list of webhooks: " + error.getMessage + error.getCause)
        false
    }
  }


  def createWebhookService(accessToken: Option[String], owner: String, repo: String) = {
    val config = Config(
      content_type = "json",
      insecure_ssl = "",
      url = WEBHOOK_ENDPOINT
    )

    val createWebhook = Github(accessToken).webhooks.createWebhooks(
      owner = owner,
      repo = repo,
      name = "web",
      active = true,
      events = Seq("pull_request"),
      config
    )

    createWebhook.exec[cats.Id, HttpResponse[String]]() match {
      case Right(webhook) =>
        log.info("Webhook Created")
        webhook.result.url.equals(WEBHOOK_ENDPOINT)
      case Left(error) =>
        log.error(s"CANNOT CREATE WEBHOOK ${error.getMessage}")
        false
    }
  }


  def listWebhookService(accessToken: Option[String], owner: String, repo: String): Future[GHResponse[List[Webhook]]] = {
    val listWebhooks = Github(accessToken).webhooks.listWebhooks(
      owner = owner,
      repo = repo
    )
    listWebhooks.execFuture[HttpResponse[String]]()
  }

  def subscribeChannelToRepoService(channelId: String, repo: String): Unit = {
      GithubRepos.subscribeChannelToRepo(channelId, repo)
  }

  def unsubscribeChannelFromRepoService(channelId: String, repo: String): Unit = {
    if (GithubRepos.isKeyExisting(repo) && GithubRepos.isChannelSubscribed(repo, channelId)) {
      GithubRepos.unsubscribeChannel(channelId, repo)
    }
  }


  def handlePullRequestEventService(event: PullRequestEvent): Option[Seq[String]] = {

    val repo = event.repository.name

    if (GithubRepos.isKeyExisting(repo) &&
      PullRequestLogic.getPullRequestAction(event) &&
      PullRequestLogic.areLabelsNonEmpty(event)
    ) {
      log.info(s"Finding Labels for $repo")
      PullRequestLogic.getMatchingLabels(event)
    }
    else {
      None
    }
  }

}
