package com.mdsol.checkmeoutbot.app.services

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import com.mdsol.checkmeoutbot.app.actors.GithubActor.{CheckForDeletedBranch, CheckForMatchingLabels, CheckForMultiplePullRequests}
import com.mdsol.checkmeoutbot.app.models.GithubModels.PullRequestEvent
import com.mdsol.checkmeoutbot.repository.GithubRepos
import com.mdsol.checkmeoutbot.utils.PullRequestsEventsConstantsUtils.{ACTION_CLOSED, ACTION_OPENED}

import scala.concurrent.duration.Duration

trait WebhookService {


  def webhookHandler(event: PullRequestEvent) = {
    val pullRequestAction = event.action
    if (GithubRepos.isKeyExisting(event.repository.name)) {

      pullRequestAction match {
        case ACTION_CLOSED => {
          if (event.pull_request.merged) {
            CheckForMatchingLabels(event)

            CheckForDeletedBranch(event)
          }
        }

        case ACTION_OPENED => {
          CheckForMultiplePullRequests(event: PullRequestEvent)
        }
      }
    }




  }

}
