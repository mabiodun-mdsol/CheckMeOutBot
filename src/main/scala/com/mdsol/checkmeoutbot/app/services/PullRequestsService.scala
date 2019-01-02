package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.app.actors.GithubActor.PullRequestsOpenedForTooLongInChannel
import com.mdsol.checkmeoutbot.app.models.GithubModels.PullRequestEvent
import com.mdsol.checkmeoutbot.app.models.SendUpdate
import com.mdsol.checkmeoutbot.config.CMOBConfig.gitApp.GITHUB_ACCESS_TOKEN
import com.mdsol.checkmeoutbot.repository.GithubRepos
import com.mdsol.checkmeoutbot.utils.{MultiplePullRequestsOpenMessage, PullRequestsOpenedForTooLongMessage}
import github4s.Github
import github4s.Github._
import github4s.free.domain.PRFilterOpen
import github4s.jvm.Implicits._
import org.joda.time.{DateTime, DateTimeZone, Days}
import scalaj.http.HttpResponse

import scala.collection.mutable.ListBuffer

trait PullRequestsService {


  def multiplePullRequestHandler(event: PullRequestEvent): Option[SendUpdate] = {


    val repo = event.repository.name
    val owner = event.repository.full_name.split("/")(0).trim
    val user = event.pull_request.user.login
    val channels = GithubRepos.getSubscribedChannels(repo)

    if (doesUserHaveMultiplePullRequestsOpened(GITHUB_ACCESS_TOKEN, user, owner, repo)) {
      Some(SendUpdate(MultiplePullRequestsOpenMessage.getMultiplePullRequestsOpenedMessage(user), channels.toList))
    }
    else {
      None
    }
  }


  def doesUserHaveMultiplePullRequestsOpened(accessToken: Option[String], userName: String, owner: String, repo: String): Boolean = {

    val prFilters = List(PRFilterOpen)
    val listPullRequests = Github(accessToken).pullRequests.list(owner, repo, prFilters)

    listPullRequests.exec[cats.Id, HttpResponse[String]]() match {
      case Left(e) => println(s"Something went wrong: ${e.getMessage}")
        false

      case Right(r) => {

        val pullRequests = r.result
        pullRequests.exists(pullRequest => pullRequest.user.get.login == userName)

      }
    }
  }


  def pullRequestOpenedTooLongHandler: Option[List[PullRequestsOpenedForTooLongInChannel]] = {

    val pullRequestsForReporting = getPullRequestsOpenedForTooLong(GITHUB_ACCESS_TOKEN).toList
    if (pullRequestsForReporting.nonEmpty) {
      Some(pullRequestsForReporting)
    }
    else {
      None
    }
  }

  def getPullRequestsOpenedForTooLong(accessToken: Option[String]): ListBuffer[PullRequestsOpenedForTooLongInChannel] = {

    val subscribedRepos = GithubRepos.getAllRepos()
    var pullRequestsThatNeedReporting: ListBuffer[PullRequestsOpenedForTooLongInChannel] = ListBuffer.empty

    for (repo <- subscribedRepos) {

      val channels = GithubRepos.getSubscribedChannels(repo)
      val prFilters = List(PRFilterOpen)
      val currentDate = DateTime.now(DateTimeZone.UTC).withTimeAtStartOfDay()
      val listPullRequests = Github(accessToken).pullRequests.list("mabiodun-mdsol", repo, prFilters)

      listPullRequests.exec[cats.Id, HttpResponse[String]]() match {
        case Left(e) => println(s"Something went wrong: ${e.getMessage}")
          None

        case Right(r) => {
          val pullRequests = r.result
          val listOfPullRequestsOpenedForTooLong = pullRequests.filter(pullRequest => isPullRequestOpenedForTooLong(pullRequest.created_at, currentDate))
          if (listOfPullRequestsOpenedForTooLong.nonEmpty) {
            pullRequestsThatNeedReporting ++= List(PullRequestsOpenedForTooLongInChannel(channels.toList, listOfPullRequestsOpenedForTooLong))
          }
        }
      }
    }
    pullRequestsThatNeedReporting
  }


  private def isPullRequestOpenedForTooLong(createdDateString: String, currentDate: DateTime): Boolean = {
    val createdDate = DateTime.parse(createdDateString).withTimeAtStartOfDay()
    val daysBetween = Days.daysBetween(createdDate, currentDate)
    daysBetween.isGreaterThan(Days.days(1))
  }


}
