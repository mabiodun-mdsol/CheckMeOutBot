package com.mdsol.checkmeoutbot.app.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.mdsol.checkmeoutbot.app.actors.GithubActor._
import com.mdsol.checkmeoutbot.app.models._
import com.mdsol.checkmeoutbot.utils.ActorRefConstantUtils._


object CMOBSupervisorActor {
  def props(): Props = Props(new CMOBSupervisorActor)
}

class CMOBSupervisorActor extends Actor with ActorLogging {

  val slackActor = context.actorOf(SlackActor.props, SLACK_ACTOR)
  val githubActor = context.actorOf(GithubActor.props(), GITHUB_ACTOR)



//  override def preStart(): Unit = {
//
//    log.info("Setting up daily reminder")
//
//    val scheduler = QuartzSchedulerExtension(ActorSystem())
//
//      scheduler.schedule(
//      "Every30Seconds",
//      githubActor,
//      CheckForPullRequestsOpenedTooLong(),
//      Option(Calendar.getInstance().getTime)
//    )
//  }


  override def receive: Receive = {

    case ProcessCommand(command, commandResponse) => {
      log.info("recieved ProcessComand")
      slackActor ! ProcessCommand(command, commandResponse)
    }
    case SlackAuthCode(code) => {
      log.info("recieved SlackAuthCode")
      slackActor ! SlackAuthCode(code)
    }
    case SubscribeCommand(channelId, text, user, responseUrl) => {
      log.info("recieved SubscribeCommand")
      slackActor ! SubscribeCommand(channelId, text, user, responseUrl)
    }
    case UnsubscribeCommand(channelid, text, repo, responseUrl) => {
      log.info("recieved UnsubscribeCommand")
      slackActor ! UnsubscribeCommand(channelid, text, repo, responseUrl)
    }
    case UnrecognisedCommand(channelId, commandText, responseUrl) => {
      log.info("recieved UnrecognisedCommand")
      slackActor ! UnrecognisedCommand(channelId, commandText, responseUrl)
    }
    case RespondToCommand(messageWriter, responseUrl) =>
      log.info("recieved RespondToCommand")
      slackActor ! RespondToCommand(messageWriter, responseUrl)

    case SendUpdate(messageWriter, channels) =>
      log.info("recieved SendUpdate")
      slackActor ! SendUpdate(messageWriter, channels)

    case GithubAuthCode(code) =>
      log.info("recieved GithubAuthoCode")
      githubActor ! GithubAuthCode(code)
    case Subscribe(owner, repo, channelId, user, responseUrl) =>
      log.info("recieved Subscribe")
      githubActor ! Subscribe(owner, repo, channelId, user, responseUrl)
    case Unsubscribe(owner, repo, channelId, user, responseUrl) =>
      log.info("recieved Unsubscribe")
      githubActor ! Unsubscribe(owner, repo, channelId, user, responseUrl)
    case ProcessWebhookEvent(pullRequestEvent) =>
      log.info("recieved ProcessWebhookEvent")
      githubActor ! ProcessWebhookEvent(pullRequestEvent)
    case CheckForMatchingLabels(event) =>
      log.info("recieved CheckForMatchingLabels")
      githubActor ! CheckForMatchingLabels(event)
    case CheckForMultiplePullRequests(event) =>
      githubActor ! CheckForMultiplePullRequests(event)
    case CheckForDeletedBranch(event) =>
      log.info("recieved CheckForDeletedBranch")
      githubActor ! CheckForDeletedBranch(event)
  }






}



