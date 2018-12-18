package com.mdsol.checkmeoutbot.routes
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import akka.actor.ActorSystem
import com.mdsol.checkmeoutbot.app.actors.CMOBSupervisorActor
import com.mdsol.checkmeoutbot.app.routes.{GithubRoutes, SlackRoutes}
import com.mdsol.checkmeoutbot.utils.ActorRefConstantUtils.CMOB_SUPERVISOR_ACTOR


class GithubRoutesTest extends WordSpec with Matchers with ScalatestRouteTest {

//  val smallRoute =
//    get {
//      pathSingleSlash {
//        complete {
//          "Captain on the bridge!"
//        }
//      } ~
//        path("ping") {
//          complete("PONG!")
//        }
//    }
//
//  "Github Routes " should {
//
//    val cmobSupervisorActor = ActorSystem().actorOf(CMOBSupervisorActor.props(), CMOB_SUPERVISOR_ACTOR)
//    val githubRoutes = new GithubRoutes(cmobSupervisorActor)
//
//
//    "return a greeting for GET requests to the root path" in {
//      // tests:
//      Post() ~> githubRoutes.webhookRoute ~> check {
//        responseAs[String] shouldEqual StatusCodes.OK
//      }
//    }

    //    "return a 'PONG!' response for GET requests to /ping" in {
    //      // tests:
    //      Get("/ping") ~> smallRoute ~> check {
    //        responseAs[String] shouldEqual "PONG!"
    //      }
    //    }
    //
    //    "leave GET requests to other paths unhandled" in {
    //      // tests:
    //      Get("/kermit") ~> smallRoute ~> check {
    //        handled shouldBe false
    //      }
    //    }
    //
    //    "return a MethodNotAllowed error for PUT requests to the root path" in {
    //      // tests:
    //      Put() ~> Route.seal(smallRoute) ~> check {
    //        status shouldEqual StatusCodes.MethodNotAllowed
    //        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: GET"
    //      }
    //    }
    //  }
//  }
}
