package com.mdsol.checkmeoutbot.app.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.mdsol.checkmeoutbot.app.actors.GithubActor.{GithubAuthCode, ProcessWebhookEvent, Subscribe, Unsubscribe}
import com.mdsol.checkmeoutbot.app.models.GithubModels.{GetGithubAccess, PullRequestEvent}
import com.mdsol.checkmeoutbot.app.models.{RespondToCommand, SendUpdate, SubscribeCommand}
import com.mdsol.checkmeoutbot.app.services._
import com.mdsol.checkmeoutbot.config.CMOBConfig.gitApp.GITHUB_ACCESS_TOKEN
import com.mdsol.checkmeoutbot.repository.GithubRepos
import com.mdsol.checkmeoutbot.utils.{LabelsCheckMessage, SubscribeMessage, UnsubscribeMessage}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


object GithubActor {
  def props(): Props = Props(new GithubActor)

  final case class Subscribe(owner: String, repo: String, channelId: String, UserId: String, responseUrl: String)

  final case class Unsubscribe(owner: String, repo: String, channelId: String, UserId: String, responseUrl: String)

  final case class ProcessWebhookEvent(pullRequestEvent: PullRequestEvent)

  final case class GithubAuthCode(code: String)

}


class GithubActor extends Actor with GithubServices with ActorLogging {


  override def preStart(): Unit = {
    log.info("Getting Github Access Token")
    handleAuthorisation()
  }


  override def receive: Receive = {
    case GithubAuthCode(code) =>
      log.info("Recieved GithubAuthCode ")
      handleAuthCode(code)
    case Subscribe(owner, repo, channelId, user, responseUrl) =>
      log.info("recieved Subscribe command")
      handleSubscribe(owner, repo, channelId, user, responseUrl)
    case Unsubscribe(owner, repo, channelId, user, responseUrl) =>
      log.info("recieved Unsubscribe Command ")
      handleUnsubscribe(owner, repo, channelId, user, responseUrl)
    case ProcessWebhookEvent(pullRequestEvent) =>
      log.info("recieved ProcessWebhookEvent ")
      handleWebhookEvent(pullRequestEvent)
  }


  def handleAuthorisation(): Unit = {
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


  def handleAuthCode(code: String): Unit = {

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


  def handleSubscribe(owner: String, repo: String, channelId: String, user: String, responseUrl: String): Unit = {

    if (GITHUB_ACCESS_TOKEN.isDefined) {
      log.info("access token is defined as: " + GITHUB_ACCESS_TOKEN.get)
      if (isWebhookCreated(GITHUB_ACCESS_TOKEN, owner, repo)) {
        log.info("webhook is already created")
        subscribeChannelToRepoService(channelId, repo)
      }
      else {
        createWebhookService(GITHUB_ACCESS_TOKEN, owner, repo)
        log.info("creating webhook")
        subscribeChannelToRepoService(channelId, repo)
      }
      sender() ! RespondToCommand(SubscribeMessage.getSubscribeMessage(repo), responseUrl)
      log.info("Sent RespondToCommandMessage to cmobSupervisorActor ")
    }
    else {
      sender() ! GetGithubAccess()
      log.info("Sent GetGithubAccess to cmobSupervisorActor ")
      sender() ! SubscribeCommand(channelId, repo, user, responseUrl)
      log.info("Sent SubscribeCommand to cmobSupervisorActor ")

    }
  }


  def handleUnsubscribe(owner: String, repo: String, channelId: String, user: String, responseUrl: String): Unit = {
    unsubscribeChannelFromRepoService(channelId, repo)
    log.info("Unsubscribed From Repo")
    sender() ! RespondToCommand(UnsubscribeMessage.getUnsubscribeMessage(repo), responseUrl)
    log.info("Sent RespondToCommand to cmobSupervisorActor ")

  }


  def handleWebhookEvent(event: PullRequestEvent): Unit = {
    val labels = handlePullRequestEventService(event)
    log.info(s"Retrieved $labels in labels from PullRequestEvent")
    if (labels.isDefined) {
      val repo = event.repository.name
      val channels = GithubRepos.getSubscribedChannels(repo)

      log.info(s"Retrieved channels: $channels that have subscribed to $repo")
      sender() ! SendUpdate(LabelsCheckMessage.getLabelsFlag(event, labels), channels.toList)
      log.info("Sent SendUpdate to cmobSupervisorActor")


    }
  }
}
