package com.mdsol.checkmeoutbot.app.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit, TestProbe}
import com.mdsol.checkmeoutbot.app.actors.GithubActor.Subscribe
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class GithubActorTests extends TestKit(ActorSystem("TestSystem")) with ImplicitSender
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


    "Listen to the Subscribe message and call the handler method" in {

      val msg = Subscribe("testOwner", "testRepo", "testChannelId", "testUserId", "testResponseUrl")
      val parent = TestProbe()
      val github = parent.childActorOf(Props(new GithubActor))
      parent.send(github, msg)
      expectNoMessage()


    }


  }




}
