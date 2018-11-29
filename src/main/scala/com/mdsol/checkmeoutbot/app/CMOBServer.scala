package com.mdsol.checkmeoutbot.app

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.mdsol.checkmeoutbot.config.CMOBConfig
import com.mdsol.checkmeoutbot.app.models.GithubModels.AuthCode
import com.mdsol.checkmeoutbot.app.actors.GithubActor

import scala.concurrent.Await

abstract class CMOBServer {


  val httpConfig = CMOBConfig.Http
  val githubConfig = CMOBConfig.gitApp

  implicit val actorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher
  implicit val githubActor = actorSystem.actorOf(Props[GithubActor], name = "GithubActor")


  val route =
    path("") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    } ~
      path("home") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "Payload"))
        }
      } ~
      path("register") {
        println("Registering...")
        get {
          parameters('code.as[String]) { (code) =>
            complete {
              githubActor ! AuthCode(code)
              "complete"
            }
          }
        }
      }

  val bindingFuture = Http().bindAndHandle(route, httpConfig.host.get, httpConfig.port.get)


  sys.addShutdownHook({
    import scala.concurrent.duration._
    Await.result(bindingFuture.map(_.unbind()), 10.seconds)
  })

}
