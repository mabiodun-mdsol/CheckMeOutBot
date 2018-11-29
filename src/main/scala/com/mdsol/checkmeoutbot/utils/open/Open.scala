package com.mdsol.checkmeoutbot.utils.open

import scala.sys.process._
import util.Properties

object Open {
  def start(input: String, application: String = null) = {
    if (Properties.isMac) {
      if (application == null || application.isEmpty) {
        Seq("open", input).!
      } else {
        Seq("open", "-a", application, input).!
      }
    } else if (Properties.isWin) {
      if (application == null || application.isEmpty) {
        Seq("start", "", input).!
      } else {
        Seq("start", "", "application", input).!
      }
    } else {
      var app = application
      if (app == null || app.isEmpty) {
        app = "xdg-open"
      }
      Seq(app, input)
    }
  }
}