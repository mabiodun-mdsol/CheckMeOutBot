package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.app.models.RespondToCommand
import com.mdsol.checkmeoutbot.config.CMOBConfig.gitApp.GITHUB_ACCESS_TOKEN
import com.mdsol.checkmeoutbot.utils.GithubAuthorisationConstantUtils.WEBHOOK_ENDPOINT
import com.mdsol.checkmeoutbot.utils.{HelpMenuMessage, SubscribeMessage}
import github4s.Github
import github4s.Github._
import github4s.free.domain.Config
import github4s.jvm.Implicits._
import org.slf4j.LoggerFactory
import scalaj.http.HttpResponse



trait SubscribeService extends RepositoryService {

  private val log = LoggerFactory.getLogger(this.getClass)


  def subscribeHandler(owner: String, repo: String, channelId: String, user: String, responseUrl: String): RespondToCommand = {

    if (isWebhookCreated(GITHUB_ACCESS_TOKEN, owner, repo)) {
      subscribeChannelToRepo(channelId, repo)
      RespondToCommand(SubscribeMessage.getSubscribeMessage(repo, user), responseUrl)
    }
    else {
      if (createWebhookService(GITHUB_ACCESS_TOKEN, owner, repo)) {
        subscribeChannelToRepo(channelId, repo)
        RespondToCommand(SubscribeMessage.getSubscribeMessage(repo, user), responseUrl)
      } else {
        RespondToCommand(HelpMenuMessage.getHelpMenuMessage(), responseUrl)
      }
    }
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

   def createWebhookService(accessToken: Option[String], owner: String, repo: String): Boolean = {
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
        log.info("Webhook Created: ", webhook.result)
        true
      case Left(error) =>
        log.error(s"CANNOT CREATE WEBHOOK ${error.getMessage}")
        false
    }
  }
}
