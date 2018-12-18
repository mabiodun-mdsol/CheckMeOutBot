package com.mdsol.checkmeoutbot.app.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.mdsol.checkmeoutbot.app.actors.GithubActor.{Subscribe, Unsubscribe}
import com.mdsol.checkmeoutbot.app.models._
import com.mdsol.checkmeoutbot.app.services.SlackServices
import com.mdsol.checkmeoutbot.utils.BaseMessageWriter

import scala.util.{Failure, Success}


object SlackActor {
  def props: Props = Props(new SlackActor)

}


class SlackActor extends Actor with SlackServices with ActorLogging {

  var token: Option[String] = None


  override def receive: Receive = {
    case SlackAuthCode(code) => {
      handleAuthCode(code)
    }

    case ProcessCommand(command, commandResponse) => {
      log.info("recievced ProcessCommand")
      handleCommand(command, commandResponse)
    }

    case SubscribeCommand(channelId, text, user, responseUrl) => {
      log.info("recieved SubscribeCommand")
      handleSubscribe(channelId, text, user, responseUrl)
    }

    case UnsubscribeCommand(channelId, text, user, responseUrl) => {
      log.info("recieved UnsubscribeCommand")
      handleUnsubscribe(channelId, text, user, responseUrl)
    }

    case UnrecognisedCommand(channelId, commandText, responseUrl) => {
      log.info("recievced UnrecognisedCommand")
      handleUnrecognisedCommand(channelId, commandText, responseUrl)
    }

    case RespondToCommand(messageWriter, responseUrl) => {
      log.info("recievced RespondToCommand ")
      handleRespondToCommand(messageWriter, responseUrl)
    }

    case SendUpdate(messageWriter: BaseMessageWriter, channels) => {
      log.info("Recieved $SendUpdate command")
      handleSendUpdate(messageWriter, channels)
    }
  }


  def handleSendUpdate(messageWriter: BaseMessageWriter, channels: List[String]) = {
    log.info(s"Sending Update...")
    sendUserEventUpdateService(token, channels, messageWriter)

  }


  def handleAuthCode(code: String): Unit = {
    val result = getAccessToken(code)
    result.onComplete {
      case Success(accessToken) =>
        println(accessToken.access_token)
        token = Some(accessToken.access_token)
      case Failure(error) => println(error)
    }
  }


  def handleCommand(command: String, commandResponse: CommandResponse): Unit = {
    command match {
      case "subscribe" =>
        sender() ! SubscribeCommand(commandResponse.channel_id, commandResponse.text, commandResponse.user_id, commandResponse.response_url)
      case "unsubscribe" =>
        sender() ! UnsubscribeCommand(commandResponse.channel_id, commandResponse.text, commandResponse.user_id, commandResponse.response_url)
      case _ =>
        sender() ! UnrecognisedCommand(commandResponse.channel_id, commandResponse.text, commandResponse.response_url)
    }
  }

  def handleUnsubscribe(channelId: String, text: String, user: String, responseUrl: String): Unit = {
    val arguments = text.split(" ").map(x => x.trim)
    sender() ! Unsubscribe(arguments(1), arguments(2), channelId, user, responseUrl)
    log.info("Sending Subscribe message to cmobSupervisorActor")
  }

  def handleSubscribe(channelId: String, text: String, user: String, responseUrl: String): Unit = {
    val arguments = text.split(" ").map(x => x.trim)
    log.info(s"recieved $arguments in command")
    sender() ! Subscribe(arguments(1), arguments(2), channelId, user, responseUrl)
    log.info("Sending Subscribe message to cmobSupervisorActor")
  }

  def handleUnrecognisedCommand(channelId: String, command: String, responseUrl: String) = {
    val text = s"the command $command is unrecognised"
    val response = postMessage(token, text, channelId)
    response.onComplete {
      case Success(value) => println(value)
      case Failure(exception) => println(exception)
    }
  }

  def handleRespondToCommand(messageWriter: BaseMessageWriter, responseUrl: String) = {
    respondToSlashCommand(messageWriter.message, responseUrl)
  }


}


