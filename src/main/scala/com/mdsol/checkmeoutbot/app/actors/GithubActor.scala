package com.mdsol.checkmeoutbot.app.actors

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.mdsol.checkmeoutbot.app.actors.GithubActor._
import com.mdsol.checkmeoutbot.app.models.GithubModels.PullRequestEvent
import com.mdsol.checkmeoutbot.app.models.{RespondToCommand, SendUpdate}
import com.mdsol.checkmeoutbot.app.services._
import com.mdsol.checkmeoutbot.repository.GithubRepos
import com.mdsol.checkmeoutbot.utils.PullRequestsEventsConstantsUtils.{ACTION_CLOSED, ACTION_OPENED}
import com.mdsol.checkmeoutbot.utils._
import github4s.free.domain.PullRequest

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


object GithubActor {
  def props(): Props = Props(new GithubActor)

  final case class Subscribe(owner: String, repo: String, channelId: String, UserId: String, responseUrl: String)

  final case class Unsubscribe(owner: String, repo: String, channelId: String, UserId: String, responseUrl: String)

  final case class ProcessWebhookEvent(pullRequestEvent: PullRequestEvent)

  final case class GithubAuthCode(code: String)

  final case class CheckForMatchingLabels(event: PullRequestEvent)

  final case class CheckForDeletedBranch(event: PullRequestEvent)

  final case class CheckForMultiplePullRequests(event: PullRequestEvent)

  final case class CheckForPullRequestsOpenedTooLong()

  final case class PullRequestsOpenedForTooLongInChannel(channels: List[String], pullRequests: List[PullRequest])

}


class GithubActor extends
  Actor with
  AuthorisationService with
  SubscribeService with
  UnsubscribeService with
  WebhookService with
  LabelsService with
  BranchService with
  PullRequestsService with
  ActorLogging {


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
    case CheckForMatchingLabels(event) =>
      log.info("recieved CheckForMatchingLabels")
      handleCheckForMatchingLabels(event)
    case CheckForDeletedBranch(event) => {
      log.info("recieved CheckForDeletedBranch")
      handleCheckForDeletedBranch(event)
    }
    case CheckForMultiplePullRequests(event) => {
      log.info("received CheckForMultiplePullRequests")
      handleCheckForMultiplePullRequests(event)
    }
    case CheckForPullRequestsOpenedTooLong() => {
      log.info("received CheckForPullRequestsOpenedTooLong")
      handleCheckForPullRequestsOpenedTooLong()
    }
  }


  def handleAuthorisation(): Unit = {
    authorisationHandler()

  }


  def handleAuthCode(code: String): Unit = {
    authCodeHandler(code)
  }

  def handleSubscribe(owner: String, repo: String, channelId: String, user: String, responseUrl: String): Unit = {
    val response: RespondToCommand = subscribeHandler(owner, repo, channelId, user, responseUrl)
    sender() ! response
    log.info("SentRespondToCommand to cmobSupervisorActor")
  }


  def handleUnsubscribe(owner: String, repo: String, channelId: String, user: String, responseUrl: String): Unit = {
    val response = unsubscribeHandler(owner, repo, channelId, user, responseUrl)
    sender() ! response
    log.info("Sent RespondToCommand to cmobSupervisorActor ")
  }


  def handleWebhookEvent(event: PullRequestEvent): Unit = {

    val pullRequestAction = event.action

    if (GithubRepos.isKeyExisting(event.repository.name)) {

      pullRequestAction match {

        case ACTION_CLOSED => {

          if (event.pull_request.merged) {
            sender() ! CheckForMatchingLabels(event)

            val actorPath = sender().path
            ActorSystem().scheduler.scheduleOnce(Duration(5, TimeUnit.SECONDS)) {
              context.actorSelection(actorPath) ! CheckForDeletedBranch(event)
            }
          }
        }

        case ACTION_OPENED => {
          sender() ! CheckForMultiplePullRequests(event: PullRequestEvent)
        }
      }
    }
  }


  def handleCheckForMatchingLabels(event: PullRequestEvent): Unit = {

    val response = labelsHandler(event: PullRequestEvent)

    if (response.isDefined)
      sender() ! response
    log.info("Sent SendUpdate to cmobSupervisorActor")
  }


  def handleCheckForDeletedBranch(event: PullRequestEvent): Unit = {
    val response = branchHandler(event)
    if (response.isDefined)
      sender() ! response
  }


  def handleCheckForMultiplePullRequests(event: PullRequestEvent): Unit = {

    val response = multiplePullRequestHandler(event)
    if (response.isDefined)
      sender() ! response

  }

  def handleCheckForPullRequestsOpenedTooLong(): Unit = {

    val response = pullRequestOpenedTooLongHandler
    if (response.isDefined) {
      for (pullRequestsForReporting <- response.get) {
        sender() !
          SendUpdate(PullRequestsOpenedForTooLongMessage.getPullRequestsOpenedForTooLongMessage(pullRequestsForReporting.pullRequests), pullRequestsForReporting.channels)
      }
    }
  }
}

