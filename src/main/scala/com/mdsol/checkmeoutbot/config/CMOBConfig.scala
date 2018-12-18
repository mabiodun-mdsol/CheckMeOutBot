package com.mdsol.checkmeoutbot.config

import com.typesafe.config.{Config, ConfigFactory}

object CMOBConfig {
  val root: Config = ConfigFactory.load(CMOBConstants.Filename.checkMeOutBot)

  object Http {
    private val HTTP_CONFIG = root.getConfig("http")
    val HOST: Option[String] = Some(HTTP_CONFIG.getString("host"))
    val PORT: Option[Int] = Some(HTTP_CONFIG.getInt("port"))
  }

  object gitApp {
    private val GIT_APP_CONFIG = root.getConfig("gitApp")
    val CLIENT_ID: Option[String] = Some(GIT_APP_CONFIG.getString("client_id"))
    val CLIENT_SECRET: Option[String] = Some(GIT_APP_CONFIG.getString("client_secret"))
    var GITHUB_ACCESS_TOKEN: Option[String] = None
  }

  object slackApp {
    private val GIT_APP_CONFIG = root.getConfig("slackApp")
    val CLIENT_ID: Option[String] = Some(GIT_APP_CONFIG.getString("client_id"))
    val CLIENT_SECRET: Option[String] = Some(GIT_APP_CONFIG.getString("client_secret"))
    var SLACK_ACCESS_TOKEN: Option[String] = None
  }

}
