package com.mdsol.checkmeoutbot.app

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.mdsol.checkmeoutbot.app.actors.CMOBSupervisorActor
import com.mdsol.checkmeoutbot.app.implicits.GlobalImplicits
import com.mdsol.checkmeoutbot.app.routes._
import com.mdsol.checkmeoutbot.config.CMOBConfig
import com.mdsol.checkmeoutbot.utils.ActorRefConstantUtils.CMOB_SUPERVISOR_ACTOR

import scala.concurrent.{Await, Future}


abstract class CMOBServer extends GlobalImplicits {


  val httpConfig: CMOBConfig.Http.type = CMOBConfig.Http

  val cmobSupervisorActor = actorSystem.actorOf(CMOBSupervisorActor.props(), CMOB_SUPERVISOR_ACTOR)
  val routes: Route = new GithubRoutes(cmobSupervisorActor).getRoutes ~ new SlackRoutes(cmobSupervisorActor).getRoutes ~ healthRoute
//  val  = scalaVersion.value

  def healthRoute: Route =
    path("app_status") {
      get {
        complete("server is running \n version 7")
      }
    }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(routes, httpConfig.HOST.get, httpConfig.PORT.get)


  


  sys.addShutdownHook({
    import scala.concurrent.duration._
    Await.result(bindingFuture.map(_.unbind()), 10.seconds)
  })

}


