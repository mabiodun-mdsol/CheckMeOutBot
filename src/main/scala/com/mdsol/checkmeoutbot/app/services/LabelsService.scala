package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.app.models.GithubModels.{Label, PullRequestEvent}
import com.mdsol.checkmeoutbot.app.models.SendUpdate
import com.mdsol.checkmeoutbot.repository.GithubRepos
import com.mdsol.checkmeoutbot.utils.LabelsCheckMessage
import com.mdsol.checkmeoutbot.utils.PullRequestsEventsConstantsUtils.LABELS
import org.slf4j.LoggerFactory

trait LabelsService {

  private val log = LoggerFactory.getLogger(this.getClass)

  def labelsHandler(event: PullRequestEvent): Option[SendUpdate] = {

    val labels = event.pull_request.labels

    if (areLabelsNonEmpty(labels)) {
      if (getMatchingLabels(labels).isDefined) {
        val repo = event.repository.name
        val channels = GithubRepos.getSubscribedChannels(repo)
        log.info(s"Retrieved channels: $channels that have subscribed to $repo")
        Some(SendUpdate(LabelsCheckMessage.getLabelsFlag(event, getMatchingLabels(labels)), channels.toList))

      } else
        None
    } else {
      None
    }
  }


  def areLabelsNonEmpty(labels: Option[Seq[Label]]) = {
    labels.nonEmpty && labels.get.nonEmpty
  }


  def getMatchingLabels(labels: Option[Seq[Label]]): Option[Seq[String]] = {
    val labelNames = labels.get collect { case x: Label => x.name.toLowerCase }
    val matchingLabels = labelNames.intersect(LABELS collect { case x: String => x.toLowerCase })
    if (matchingLabels.nonEmpty) {
      Some(matchingLabels)
    }
    else {
      None
    }
  }
}
