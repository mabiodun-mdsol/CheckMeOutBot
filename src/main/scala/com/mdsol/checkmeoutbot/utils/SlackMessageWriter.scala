package com.mdsol.checkmeoutbot.utils

import com.mdsol.checkmeoutbot.app.models.GithubModels.PullRequestEvent
import github4s.free.domain.PullRequest
import slack.models.{ActionField, Attachment, AttachmentField}


class SlackMessageWriter {

}


trait BaseMessageWriter {

  val text: Option[String]

  val fallback: Option[String] = None

  val callbackId: Option[String] = None

  val pretext: Option[String] = None

  val title: Option[String] = None

  val title_link: Option[String] = None

  val color: Option[String] = Some("#36a64f")

  val author_name = Some("CheckMeOutBot")

  val author_link = None

  val authoer_icon = None

  val fields = None
  val image_url = None
  val thumb_url = None
  val actions = None
  val mrkdwn_in = None


  def createAttachment: Option[Seq[Attachment]] = Some(Seq(Attachment(fallback, callbackId, color, pretext, author_name, author_link, authoer_icon, title, title_link, text, fields, image_url, thumb_url, actions, mrkdwn_in)))


  val attachment = Attachment(fallback, callbackId, color, pretext, author_name, author_link, authoer_icon, title, title_link, text, fields, image_url, thumb_url, actions, mrkdwn_in)

}


object SubscribeMessage {

  def getSubscribeMessage(repo: String, user: String) =
    new SubscribeMessage(repo, user)
}


class SubscribeMessage(repo: String, user: String) extends BaseMessageWriter {

  override val text = Some(s"@$user has successfully subscribed to this $repo")

  createAttachment
}


object UnsubscribeMessage {

  def getUnsubscribeMessage(repo: String) =
    new UnsubscribeMessage(repo)
}


class UnsubscribeMessage(repo: String) extends BaseMessageWriter {
  override val text = Some(s"You have successfully unsubscribed to this ${repo}")


}


object LabelsCheckMessage {

  def getLabelsFlag(event: PullRequestEvent, labels: Option[Seq[String]]) =
    new LabelsCheckMessage(event, labels)

}

class LabelsCheckMessage(event: PullRequestEvent, labels: Option[Seq[String]]) extends BaseMessageWriter {
  override val text = Some(s"your repo ${event.repository.name} was merged with the following flags ${labels.get.mkString(" ")}")

}


object BranchNotDeletedMessage {
  def getBranchNotDeletedMessage(branch: String) = {
    new BranchNotDeletedMessage(branch)
  }
}

class BranchNotDeletedMessage(branch: String) extends BaseMessageWriter {

  override val text = Some(s"Your branch $branch has been merged, don't forget to delete the branch")

}


object MultiplePullRequestsOpenMessage {

  def getMultiplePullRequestsOpenedMessage(user: String) = {
    new MultiplePullRequestsOpenMessage(user)
  }
}

class MultiplePullRequestsOpenMessage(user: String) extends BaseMessageWriter {

  override val text = Some(s"$user has multiple pull requests opened, please get them reviewed and closed")

}


object PullRequestsOpenedForTooLongMessage {

  def getPullRequestsOpenedForTooLongMessage(pullRequests: List[PullRequest]) = {
    new PullRequestsOpenedForTooLongMessage(pullRequests)
  }
}

class PullRequestsOpenedForTooLongMessage(pullRequests: List[PullRequest]) extends BaseMessageWriter {

  override val text = Some(s"The following pull requests have been opened for more than a day: \n")
  val prs = s"${pullRequests.map(_.html_url.mkString("/n"))}"
}


object HelpMenuMessage {

  def getHelpMenuMessage(): HelpMenuMessage = {
    new HelpMenuMessage()
  }
}

class HelpMenuMessage() extends BaseMessageWriter {
  override val text: Option[String] = Some("help menu menssage")
}

