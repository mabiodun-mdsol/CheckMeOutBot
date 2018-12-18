package com.mdsol.checkmeoutbot.app.implicits

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.ActorMaterializer
import com.mdsol.checkmeoutbot.app.actors.CMOBSupervisorActor
import com.mdsol.checkmeoutbot.utils.ActorRefConstantUtils._




trait GlobalImplicits {

  implicit val actorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher
}


//trait GithubActorImplicits extends  GlobalImplicits{
//  val slackActor: ActorSelection = actorSystem.actorSelection(s"../$SLACK_ACTOR")
//  val githubActor: ActorSelection = actorSystem.actorSelection(s"../$GITHUB_ACTOR")
//}
//
//trait SlackActorImplicits extends GlobalImplicits {
//  val slackActor: ActorSelection = actorSystem.actorSelection(s"../$SLACK_ACTOR")
//  val githubActor: ActorSelection = actorSystem.actorSelection(s"../$GITHUB_ACTOR")
//
//
//
//}






