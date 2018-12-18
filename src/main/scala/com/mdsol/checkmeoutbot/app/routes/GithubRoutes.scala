package com.mdsol.checkmeoutbot.app.routes

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.mdsol.checkmeoutbot.app.actors.GithubActor.{GithubAuthCode, ProcessWebhookEvent}
import com.mdsol.checkmeoutbot.app.implicits.GlobalImplicits
import com.mdsol.checkmeoutbot.app.models.GithubModels.PullRequestEvent
import com.mdsol.checkmeoutbot.utils.RouteConstantUtils._
import io.circe.Printer
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.Printer._
import io.circe.syntax._
import io.circe.generic.auto._


class GithubRoutes(cmobSupervisorActor: ActorRef) extends GlobalImplicits {

  import org.slf4j.LoggerFactory

  private val log = LoggerFactory.getLogger(this.getClass)


  def getRoutes: Route = authenticatingRoute ~ webhookRoute

  def webhookRoute: Route =
    path(GITHUB_WEBHOOK) {
      post {
        entity(as[String]) {
          log.info("Recieved payload from github webhook ... \n attempting to decode")
          x =>
            val decodedFoo = decode[PullRequestEvent](x)
            decodedFoo match {
              case Right(pullRequestEvent) => {
                log.info("Decoding Successful ...")
                cmobSupervisorActor ! ProcessWebhookEvent(pullRequestEvent)
                log.info(pullRequestEvent.asJson.pretty(Printer.spaces2))
              }
              case Left(y) => {
                log.info("Issue decoding json")
                log.info(y.toString)
              }
            }
            complete(StatusCodes.OK)
        }
      }
    }


  def authenticatingRoute: Route = {

    path(GITHUB_AUTHENTICATE) {
      println("Authenticating...")
      get {
        parameters('code.as[String]) { code =>
          complete {
            cmobSupervisorActor ! GithubAuthCode(code)
            code
          }
        }
      }
    }
  }

}

