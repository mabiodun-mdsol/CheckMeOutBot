package com.mdsol.checkmeoutbot.app.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit, TestProbe}
import com.mdsol.checkmeoutbot.app.actors.GithubActor.Subscribe
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class CMOBActorTests extends TestKit(ActorSystem("TestSystem")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with MockitoSugar {


  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A Github actor" should {

    "send back messages unchanged" in {
      val github = system.actorOf(TestActors.echoActorProps)
      github ! "hello world"
      expectMsg("hello world")
    }


    "Recieve the Subscribe message" in {

      val msg = Subscribe("testOwner", "testRepo", "testChannelId", "testUserId", "testResponseUrl")
      val parent = TestProbe()
      val cmob = parent.childActorOf(Props(new CMOBSupervisorActor))
      parent.send(cmob, msg)
      expectNoMessage()
    }

  }


}
