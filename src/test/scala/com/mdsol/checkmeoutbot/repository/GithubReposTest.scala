package com.mdsol.checkmeoutbot.repository

import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.ListBuffer

class GithubReposTest extends WordSpec with Matchers {


  "#GithubRepo" should {


    "subscribe to repo" in {

      GithubRepos.userReposSubscriptions = Map.empty

      GithubRepos.subscribeChannelToRepo("channel 1", "repo 1")

      val expected: Map[String, ListBuffer[String]] = Map("repo 1" -> ListBuffer("channel 1"))

      GithubRepos.userReposSubscriptions shouldBe expected

    }

    "unsubscribe to repo" in {


      GithubRepos.userReposSubscriptions = Map("repo 1" -> ListBuffer("channel 1"))

      GithubRepos.subscribeChannelToRepo("channel 1", "repo 1")
      GithubRepos.subscribeChannelToRepo("channel 2", "repo 3")


      GithubRepos.unsubscribeChannel("channel 1", "repo 1")

      val expected: Map[String, ListBuffer[String]] = Map("repo 3" -> ListBuffer("channel 2"))

      GithubRepos.userReposSubscriptions shouldBe expected


    }


    "check if channel is subscribed" in{

      GithubRepos.userReposSubscriptions =  Map("repo 1" -> ListBuffer("channel 1"))
      val actual = GithubRepos.isChannelSubscribed("repo 1", "channel 1")

      actual shouldBe true
    }



  }
}


//
//  before(deleteFile)
//
//
//  def deleteFile = {
//    val fileTemp = new File(RepoConstantsUtils.FILE_NAME)
//    if (fileTemp.exists) {
//      fileTemp.delete()
//    }
//  }
//
//  "#GithubRepos" should {
//
//    //delete file
//
//
//    //set up
//    val githubRepos = new GithubRepos()
//
////
////    "add a user to an existing repo" in {
////      //arrange
////
////      val repo1 = "repo 1"
////      val user1 = " user 1"
////
////
////      //act
////      val result = githubRepos.subscribeChannelToRepo()
////      //assert
////      result shouldBe true
////    }
////
////    "add a user to a repo which doesn't exist" in {
////      val result = githubRepos.subscribeChannelToRepo()
////      result shouldBe None
////
////    }
////
////
////    "add a repo and a user when repo does exist" in {
////      //arrange
////
////      //act
////
////      //assert
////    }
//
//
//    "add a repo and a user when repo doesn't exist" in {
//
//      val githubRepos = new GithubRepos()
//
//
//      val expectedJson: String =
//        """
//          |{
//          | "repo 1": [
//          |   "channel 1"
//          | ]
//          |}
//        """.stripMargin
//
//      val result = githubRepos.subscribeChannelToRepo("repo 1", "channel 1")
//      val expected = Some(expectedJson)
//
//      result.get.toString.filterNot(x => x.isWhitespace) shouldBe expected.get.toString.trim.filterNot(x => x.isWhitespace)
//
//    }
//
//
//  }
//
//
//}
