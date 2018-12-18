package com.mdsol.checkmeoutbot.repository

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
    }
  }

    def getSubscribedChannels(repo: String) = {
      userReposSubscriptions(repo)
    }

    def isChannelSubscribed(repo: String, channel: String) = {
      userReposSubscriptions(repo).exists(x => x.equalsIgnoreCase(channel))
    }

  }
