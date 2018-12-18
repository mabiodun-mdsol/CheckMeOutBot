package com.mdsol.checkmeoutbot.app.routes

import akka.actor.ActorRef
import akka.http.scaladsl.model.FormData
import akka.http.scaladsl.server.{Directives, Route}
import com.mdsol.checkmeoutbot.app.implicits.GlobalImplicits
import com.mdsol.checkmeoutbot.app.models.SlackModels._
import com.mdsol.checkmeoutbot.app.models.{CommandResponse, ProcessCommand, SlackAuthCode, SlackModelSupport}
import com.mdsol.checkmeoutbot.utils.RouteConstantUtils._
import spray.json._


class SlackRoutes(cmobSupervisorActor: ActorRef) extends Directives with SlackModelSupport with GlobalImplicits {

  import org.slf4j.LoggerFactory

  private val log = LoggerFactory.getLogger(this.getClass)

  def getRoutes = commandsRoute ~ eventsRoute ~ interactionsRoute ~ authenticationRoute

  private def commandsRoute: Route =

    path(SLACK_COMMANDS) {
      post {
        entity(as[FormData]) {
          x => {
            val commandResponse = x.fields.toMap.toJson.convertTo[CommandResponse]
            log.info("recieved payload from slack command endpoint")
            log.info(commandResponse.toJson.prettyPrint)
            cmobSupervisorActor ! ProcessCommand(commandResponse.text.split(" ")(0), commandResponse)
            log.info("Sent cmobSupervisorActor a message to process the command")
          }
            complete("")
        }
      }
    }

  private def eventsRoute: Route =
    path(SLACK_EVENTS) {
      post {
        entity(as[EventVerification]) {
          x =>
            println(x.challenge)
            complete(x.challenge)
        }
      }
    }

  private def interactionsRoute: Route =
    path(SLACK_INTERACTIONS) {
      post {
        entity(as[EventVerification]) {
          x =>
            println(x.challenge)
            complete(x.challenge)
        }
      }
    }

  private def authenticationRoute: Route =
    path(SLACK_AUTHENTICATE) {
      log.info("Authenticating slack ...")
      get {
        parameters('code.as[String]) { (code) =>
          complete {
            log.info("Sending message to cmobSupervisorActor")
            cmobSupervisorActor ! SlackAuthCode(code)
            println(code)
            "complete"
          }
        }
      }
    }
}

