package com.mdsol.checkmeoutbot.app.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.mdsol.checkmeoutbot.utils.ConstantUtils._

class GithubRoutes {


  def getRoutes: Route = authenticateRoute


  private val authenticateRoute: Route =
    get {
      path(authenticate) {
        parameters('code) { code =>
          println(code)
          complete("registered")
        }
      }
    }

  private val webhookRoute: Route =
    get {
      path(webhook) {
        parameters('code) { code =>
          println(code)
          complete("recievedEvent")
        }
      }
    }

}

