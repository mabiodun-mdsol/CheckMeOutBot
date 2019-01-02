package com.mdsol.checkmeoutbot.repository

import scala.collection.mutable.ListBuffer

object DailyReportRepo {

case class DailyReport(channel: String, numberOfDays: Int)

  val dailyReportSubscribers: ListBuffer[DailyReport] = ListBuffer.empty


  def addChannel(channel: String, numberOfDays: Int): dailyReportSubscribers.type = {
    dailyReportSubscribers ++= List(DailyReport(channel, numberOfDays))
  }

  def removeChannel(channel: String): Unit = {
    dailyReportSubscribers --= dailyReportSubscribers.filter(x => x.channel == channel)
  }

}
