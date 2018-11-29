package com.mdsol.checkmeoutbot.config

import com.typesafe.config.{Config, ConfigFactory}

object CMOBConfig {
  val root: Config = ConfigFactory.load(CMOBConstants.Filename.checkMeOutBot)

  object Http {
    private val httpConfig = root.getConfig("http")
    val host: Option[String] = Some(httpConfig.getString("host"))
    val port: Option[Int] = Some(httpConfig.getInt("port"))
  }

  object gitApp {
    private val gitAppConfig = root.getConfig("gitApp")
    val clientId: Option[String] = Some(gitAppConfig.getString("client_id"))
    val clientSecret: Option[String] = Some(gitAppConfig.getString("client_secret"))
  }

}
