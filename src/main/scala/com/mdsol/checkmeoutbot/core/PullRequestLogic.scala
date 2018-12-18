package com.mdsol.checkmeoutbot.core

import com.mdsol.checkmeoutbot.app.models.GithubModels.{Label, PullRequestEvent}
import com.mdsol.checkmeoutbot.utils.PullRequestsEventsConstants._
import org.slf4j.LoggerFactory

object PullRequestLogic {

  private val log = LoggerFactory.getLogger(this.getClass)


  def getPullRequestAction(event: PullRequestEvent): Boolean = {


    val pullRequestAction = event.action
    val pullRequestMerged = event.pull_request.merged

    log.info(s"Pull Request is $pullRequestAction merged status is $pullRequestMerged")
    pullRequestAction.equalsIgnoreCase(ACTION_CLOSED) && pullRequestMerged
  }

  def areLabelsNonEmpty(event: PullRequestEvent) = {
    event.pull_request.labels.nonEmpty && event.pull_request.labels.get.nonEmpty
  }


  def getMatchingLabels(event: PullRequestEvent): Option[Seq[String]] = {
    val labels = event.pull_request.labels.get
    val labelNames = labels collect { case x: Label => x.name.toLowerCase }
    val matchingLabels = labelNames.intersect(LABELS collect { case x: String => x.toLowerCase })
    if (matchingLabels.nonEmpty) {
      Some(matchingLabels)
    }
    else {
      None
    }
  }
}
