package com.mdsol.checkmeoutbot.app.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.mdsol.checkmeoutbot.app.models.SlackModels.EventVerification
import com.mdsol.checkmeoutbot.utils.BaseMessageWriter
import spray.json.DefaultJsonProtocol


object SlackModels {

  final case class EventVerification(token: String, challenge: String, `type`: String)

  final case class ConfirmSubscription(user: String, channelId: String, githubRepo: String, `type`: String)

}


trait SlackModelSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val eventVerificationJsonFormat = jsonFormat3(EventVerification)
  implicit val commandResponseJsonFormat = jsonFormat11(CommandResponse)
}

case class SlackAuthCode(code: String)


case class CommandResponse(
                            channel_name: String,
                            team_id: String,
                            response_url: String,
                            trigger_id: String,
                            text: String,
                            command: String,
                            user_id: String,
                            channel_id: String,
                            token: String,
                            team_domain: String,
                            user_name: String
                          )

case class ProcessCommand(command: String, commandResponse: CommandResponse)

case class SubscribeCommand(channelId: String, githubRepoOrOrg: String, user: String, responseUrl: String)

case class UnsubscribeCommand(channelId: String, githubRepoOrOrg: String, user: String, responseUrl: String)

case class UnrecognisedCommand(channelId: String, githubRepoOrOrg: String, responseUrl: String)

case class RespondToCommand(messageWriter: BaseMessageWriter, responseUrl: String)

case class SendUpdate(messageWriter: BaseMessageWriter, channels: List[String])

case class DailyReportCommand(isOn: Boolean, channelId: String, numberOfDays: Int)
