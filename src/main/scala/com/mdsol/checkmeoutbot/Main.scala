package com.mdsol.checkmeoutbot

import com.mdsol.checkmeoutbot.app.CMOBServer
import com.mdsol.checkmeoutbot.app.controllers.AuthenticateController

object Main extends CMOBServer with App {



  val command = scala.io.StdIn.readLine()
  val authenticator = new AuthenticateController

  def installCheckMeOutBot() = {
    println("Installing ...")
    authenticator.authenticate()


  }

  def getAccessToken(): Unit = {
    println("getting access token ...")
  }


  def subscribeToCheckMeOutBot(command: String) = ???

  command.split(" ")(0) match {
    case "install" => installCheckMeOutBot()
    case "subscribe" => subscribeToCheckMeOutBot(command)
    case "access" => getAccessToken()
  }



}
