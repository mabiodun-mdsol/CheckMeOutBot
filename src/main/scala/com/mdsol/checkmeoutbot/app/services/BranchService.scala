package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.app.models.GithubModels.PullRequestEvent
import com.mdsol.checkmeoutbot.app.models.SendUpdate
import com.mdsol.checkmeoutbot.config.CMOBConfig.gitApp.GITHUB_ACCESS_TOKEN
import com.mdsol.checkmeoutbot.utils.BranchNotDeletedMessage
import github4s.Github
import github4s.Github._
import github4s.jvm.Implicits._
import org.slf4j.LoggerFactory
import scalaj.http.HttpResponse



trait BranchService extends RepositoryService {


  private val log = LoggerFactory.getLogger(this.getClass)


  def branchHandler(event: PullRequestEvent): Option[SendUpdate] = {

    val repo = event.repository.name
    val owner = event.repository.full_name.split("/")(0).trim
    val branchName = event.pull_request.head.ref

    val channels = getSubscribedChannels(repo)

    if (GITHUB_ACCESS_TOKEN.isDefined && doesBranchStillExist(GITHUB_ACCESS_TOKEN, branchName, owner, repo)) {
      Some(SendUpdate(BranchNotDeletedMessage.getBranchNotDeletedMessage(branchName), channels.toList))
    }
    else {
      None
    }
  }


  def doesBranchStillExist(accessToken: Option[String], branchName: String, owner: String, repo: String):Boolean = {

    val listBranches = Github(accessToken).repos.listBranches(owner, repo)
    listBranches.exec[cats.Id, HttpResponse[String]]() match {
      case Left(e) =>
        log.error(s"Something went wrong: ${e.getMessage}")
        false
      case Right(r) => {
        val branches = r.result
        branches.exists(branch => branch.name == branchName)
      }
    }
  }
}
