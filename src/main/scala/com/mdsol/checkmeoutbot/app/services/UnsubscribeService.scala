package com.mdsol.checkmeoutbot.app.services

import com.mdsol.checkmeoutbot.app.models.RespondToCommand
import com.mdsol.checkmeoutbot.utils.UnsubscribeMessage
import org.slf4j.LoggerFactory

trait UnsubscribeService extends RepositoryService {

  private val log = LoggerFactory.getLogger(this.getClass)


  def unsubscribeHandler(owner: String, repo: String, channelId: String, user: String, responseUrl: String): RespondToCommand = {
    unsubscribeChannel(channelId, repo)
    log.info("Unsubscribed From Repo")
    RespondToCommand(UnsubscribeMessage.getUnsubscribeMessage(repo), responseUrl)
  }

}
