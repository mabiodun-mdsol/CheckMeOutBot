package com.mdsol.checkmeoutbot.repository

//
//import java.io.{File, FileNotFoundException, PrintWriter}
//
//import com.mdsol.checkmeoutbot.utils.RepoConstantsUtils
//import io.circe._
//import io.circe.parser._
//import io.circe.syntax._
//import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


object GithubRepos {

  var userReposSubscriptions: Map[String, ListBuffer[String]] = Map.empty


  def subscribeChannelToRepo(channel: String, repo: String): Unit = {
    if (userReposSubscriptions.get(repo).isDefined) {
      if (!isChannelSubscribed(repo, channel)) {
        userReposSubscriptions(repo) ++= List(channel)
      }
    }
    userReposSubscriptions += (repo -> mutable.ListBuffer(channel))
  }


  def isKeyExisting(repo: String) = userReposSubscriptions.keySet.contains(repo)


  def unsubscribeChannel(channel: String, repo: String): Unit = {

    if (isKeyExisting(repo) && isChannelSubscribed(repo, channel)) {
      val indexPosition = userReposSubscriptions(repo).indexOf(channel)

      userReposSubscriptions(repo).remove(indexPosition)
      if (userReposSubscriptions(repo).isEmpty) {
        userReposSubscriptions -= repo
      }
    }
  }

  def getSubscribedChannels(repo: String) = {
    userReposSubscriptions(repo)
  }

  def isChannelSubscribed(repo: String, channel: String) = {
    userReposSubscriptions(repo).exists(x => x.equalsIgnoreCase(channel))
  }

  def getAllRepos() = {
    userReposSubscriptions.keys
  }

}

//
//
//class GithubRepos {
//
//
//  private val doc: Json = readRepoMethod().get
//
//
//  private val log = LoggerFactory.getLogger(this.getClass)
//
//  def subscribeChannelToRepo(repo: String, channel: String) = {
//    val cursor: HCursor = doc.hcursor
//    cursor.downField(repo) match {
//      case _: FailedCursor =>
//        writeRepoFile(addRepoAndChannel(channel, repo))
//      case _ =>
//        writeRepoFile(addChannel(channel, repo))
//    }
//  }
//
//
//  private def readRepoMethod(): Option[Json] = {
//
//    try {
//      val readFile = parse(scala.io.Source.fromFile(RepoConstantsUtils.FILE_NAME).mkString)
//      readFile match {
//        case Right(json) =>
//          Some(json)
//        case Left(x) =>
//          println(x)
//          None
//      }
//    }
//    catch {
//      case _: FileNotFoundException => {
//        writeRepoFile(None)
//      }
//    }
//  }
//
//
//  private def writeRepoFile(doc: Option[Json]): Option[Json] = {
//    val file = new File(RepoConstantsUtils.FILE_NAME)
//    val pw = new PrintWriter(file)
//    val content = doc.getOrElse(JsonObject.empty.asJson)
//    pw.write(content.pretty(Printer.spaces2))
//    pw.checkError()
//    pw.close()
//    Some(content)
//  }
//
//
//  private def addChannel(channel: String, repo: String) = {
//    val cursor: HCursor = doc.hcursor
//    val vectorList: Vector[Json] = Vector(channel.asJson)
//    cursor.downField(repo).downArray.last.setRights(vectorList).top
//  }
//
//  private def addRepoAndChannel(channel: String, repo: String) = {
//    Some(doc.asObject.map(_.+:(repo, List(channel).asJson)).asJson)
//  }
//
//
//  def getSubscribers(repo: String): List[Json] = {
//    doc.findAllByKey(repo)
//  }
//
//
//  def unsubscribeChannel(channel: String, repo: String, doc: Json): Option[Json] = {
//    val cursor: HCursor = doc.hcursor
//    cursor.downField(repo).downArray.find(x => x == channel.asJson) match {
//      case _: FailedCursor =>
//        None
//      case x => {
//        writeRepoFile(x.delete.top)
//      }
//    }
//  }
//}
