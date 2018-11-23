import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class Server extends App {


  implicit val actorSystem = ActorSystem("GithubActor")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher


  val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().bindAndH(interface = "localhost", port = 8080)

  val bindingFuture: Future[Http.ServerBinding] =
    serverSource.to(Sink.foreach{ connection =>
      println("Accepted new connection from " + connection.remoteAddress )
    }).run()


}
