package com.mdsol.checkmeoutbot.app.services

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

trait RepositoryService {

  var userReposSubscriptions: Map[String, ListBuffer[String]] = Map.empty


  def subscribeChannelToRepo(channel: String, repo: String): Unit = {
    if (userReposSubscriptions.get(repo).isDefined) {
      if (!isChannelSubscribed(repo, channel)) {
        userReposSubscriptions(repo) ++= List(channel)
      }
    }
    userReposSubscriptions += (repo -> mutable.ListBuffer(channel))
  }


  def isKeyExisting(repo: String): Boolean = userReposSubscriptions.keySet.contains(repo)


  def unsubscribeChannel(channel: String, repo: String): Unit = {

    if (isKeyExisting(repo) && isChannelSubscribed(repo, channel)) {
      val indexPosition = userReposSubscriptions(repo).indexOf(channel)

      userReposSubscriptions(repo).remove(indexPosition)
      if (userReposSubscriptions(repo).isEmpty) {
        userReposSubscriptions -= repo
      }
    }
  }

  def getSubscribedChannels(repo: String): ListBuffer[String] = {
    userReposSubscriptions(repo)
  }

  def isChannelSubscribed(repo: String, channel: String): Boolean = {
    userReposSubscriptions(repo).exists(x => x.equalsIgnoreCase(channel))
  }

  def getAllRepos(): Iterable[String] = {
    userReposSubscriptions.keys
  }


}
