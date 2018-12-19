package com.mdsol.checkmeoutbot.utils
import akka.http.scaladsl.server.Directives._
import com.mdsol.checkmeoutbot.config.CMOBConfig

object RouteConstantUtils {

  val GITHUB_AUTHENTICATE = "github" / "authenticate"
  val GITHUB_WEBHOOK = "github" / "cmob" / "webhook"
  val SLACK_COMMANDS = "slack" / "commands"
  val SLACK_AUTHENTICATE = "slack" / "authenticate"
  val SLACK_EVENTS = "slack" / "events"
  val SLACK_INTERACTIONS = "slack" / "interactions"
}


object ActorRefConstantUtils {
  val SLACK_ACTOR = "SlackActor"
  val GITHUB_ACTOR = "GithubActor"
  val CMOB_SUPERVISOR_ACTOR = "CMOBSupervisorActor"
}

object GithubAuthorisationConstantUtils {
  val clientId = CMOBConfig.gitApp.CLIENT_ID.get
  val GITHUB_AUTH_URL = s"https://github.com/login/oauth/authorize?scope=write:repo_hook&client_id=$clientId"
  val WEBHOOK_ENDPOINT = s"https://checkmeout-sandbox.imedidata.net/github/cmob/webhook"
//  val WEBHOOK_ENDPOINT = s"https://dimitto.serveo.net/github/cmob/webhook"
}

object PullRequestsEventsConstants {
  val ACTION_MERGED = "merged"
  val ACTION_PUSHED = "pushed"
  val ACTION_CLOSED = "closed"
  val LABELS = List("do not merge")
}