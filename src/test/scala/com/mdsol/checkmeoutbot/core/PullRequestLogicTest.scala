package com.mdsol.checkmeoutbot.core


import com.mdsol.checkmeoutbot.app.models.GithubModels.{Label, PullRequest, PullRequestEvent}
import org.mockito.integrations.scalatest.MockitoFixture
import org.scalatest.{Matchers, WordSpec}

class PullRequestLogicTest extends WordSpec with Matchers with MockitoFixture {

  "#PullRequestLogic" should {

    val mockPullRequestEvent = mock[PullRequestEvent]
    val mockPullRequest = mock[PullRequest]
    when(mockPullRequestEvent.pull_request).thenReturn(mockPullRequest)


    val labels = Label(
      1,
      "node_id_1",
      "www.fakeurl.com/1",
      "do not merge",
      Some("do not merge this branch"),
      "red",
      default = false)




    "return false if labels are empty" in {

      when(mockPullRequestEvent.pull_request.labels).thenReturn(Some(Seq()))

      val result = PullRequestLogic.areLabelsNonEmpty(mockPullRequestEvent)

      result shouldBe false
    }


    "return true if labels are not empty" in {
      when(mockPullRequestEvent.pull_request.labels).thenReturn(Some(Seq(labels)))

      val result = PullRequestLogic.areLabelsNonEmpty(mockPullRequestEvent)

      result shouldBe true
    }


    "returns the matching labels" in {


      when(mockPullRequestEvent.pull_request.labels).thenReturn(Some(Seq(labels)))
      val result = PullRequestLogic.getMatchingLabels(mockPullRequestEvent)
      result shouldBe Some(List("do not merge"))

    }
  }


}
