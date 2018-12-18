package com.mdsol.checkmeoutbot.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import com.mdsol.checkmeoutbot.app.actors.GithubActor

class GithubActorTests extends TestKit(ActorSystem("TestSystem")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {


  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A Github actor" must {

    "send back messages unchanged" in {
      val github = system.actorOf(TestActors.echoActorProps)
      github ! "hello world"
      expectMsg("hello world")
    }
  }




}
