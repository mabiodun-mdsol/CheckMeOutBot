package com.mdsol.checkmeoutbot.utils

import com.mdsol.checkmeoutbot.app.models.GithubModels.PullRequestEvent
import slack.models.Attachment


class SlackMessageWriter {

}


trait BaseMessageWriter {
  def message: String
}


object SubscribeMessage {

  def getSubscribeMessage(repo: String) =
    new SubscribeMessage(repo)
}


class SubscribeMessage(repo: String) extends  BaseMessageWriter {
  val text = s"You have successfully subscribed to this ${repo}"

  override def message: String = text
}


object UnsubscribeMessage {

  def getUnsubscribeMessage(repo: String) =
    new UnsubscribeMessage(repo)
}


class UnsubscribeMessage(repo: String) extends BaseMessageWriter {
  val text = s"You have successfully unsubscribed to this ${repo}"

  override def message: String = text
}



object LabelsCheckMessage {

  def getLabelsFlag(event: PullRequestEvent, labels:Option[Seq[String]]) =
    new LabelsCheckMessage(event, labels)

}

class LabelsCheckMessage(event: PullRequestEvent, labels:Option[Seq[String]]) extends BaseMessageWriter {
  val text = s"your repo ${event.repository.name} was merged with the following flags ${labels.get.mkString(" ")}"

  override def message: String = text
}
