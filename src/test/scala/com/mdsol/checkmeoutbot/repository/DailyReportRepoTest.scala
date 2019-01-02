package com.mdsol.checkmeoutbot.repository

import com.mdsol.checkmeoutbot.repository.DailyReportRepo.DailyReport
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.ListBuffer

class DailyReportRepoTest extends WordSpec with Matchers {


  "#Daily report repo" should {


    "add a channel" in {

      DailyReportRepo.dailyReportSubscribers == ListBuffer.empty

      DailyReportRepo.addChannel("channel 1", 1)

      val expectedValue = ListBuffer(DailyReport("channel 1", 1))

      DailyReportRepo.dailyReportSubscribers shouldBe expectedValue
    }


    "remove a channel" in {

      DailyReportRepo.dailyReportSubscribers == ListBuffer.empty

      DailyReportRepo.addChannel("channel 1", 1)

      DailyReportRepo.removeChannel("channel 1")

      val expectedvalue = ListBuffer.empty[DailyReport]

      DailyReportRepo.dailyReportSubscribers shouldBe expectedvalue


    }


  }
}
