package com.mdsol.checkmeoutbot.app.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit, TestProbe}
import com.mdsol.checkmeoutbot.app.actors.GithubActor.Subscribe
import com.mdsol.checkmeoutbot.app.models.{CommandResponse, ProcessCommand}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class SlackActorTests extends TestKit(ActorSystem("TestSystem")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with MockitoSugar {


  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A Slack actor" should {

    "send back messages unchanged" in {
      val slack = system.actorOf(TestActors.echoActorProps)
      slack ! "hello world"
      expectMsg("hello world")
    }


    "Recieve a message of type ProcessCommand" in {

      val msg = ProcessCommand("command", CommandResponse("test", "test","test","test","test","test","test","test","test","test","test") )
      val parent = TestProbe()
      val slack = parent.childActorOf(Props(new SlackActor))
      parent.send(slack, msg)
      expectNoMessage()

    }


  }




}
