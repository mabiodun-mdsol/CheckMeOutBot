package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.app.implicits.GlobalImplicits
import com.mdsol.checkmeoutbot.config.CMOBConfig
import com.mdsol.checkmeoutbot.utils.BaseMessageWriter
import slack.api.{AccessToken, SlackApiClient}

import scala.concurrent.Future

trait SlackServices extends GlobalImplicits {

  val slackConfig = CMOBConfig.slackApp

  def checkToken(accessToken: Option[String]): Boolean = {
    accessToken.isDefined
  }

  def getAccessToken(code: String): Future[AccessToken] = {
    SlackApiClient.exchangeOauthForToken(
      slackConfig.CLIENT_ID.get,
      slackConfig.CLIENT_SECRET.get,
      code
    )
  }

  def postMessage(accessToken: Option[String], text: String, channelId: String) = {
    SlackApiClient(accessToken.get).postChatMessage(channelId, text)
  }

  def respondToSlashCommand(messageWriter: BaseMessageWriter, responseUrl: String) = {
    SlackApiClient.respondToSlashCommand(null, responseUrl, "ephemeral", messageWriter.createAttachment)
  }

  def sendUserEventUpdateService(accessToken: Option[String], channels: List[String], messageWriter: BaseMessageWriter) = {
    for (channel <- channels) {
      SlackApiClient(accessToken.get).postChatMessage(channel, messageWriter.text.get)
    }
  }
}
