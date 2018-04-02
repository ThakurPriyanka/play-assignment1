package views

import akka.util.Timeout
import forms.ProfileForm
import models.UserInfo
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.Flash
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, contentAsString}

import scala.concurrent.duration._

class ProfileTest extends PlaySpec with Mockito {
  "Rending add profile page" in new App {
    implicit val duration: Timeout = 5 seconds
    val request = FakeRequest(GET, "/profile")
    val flash = mock[Flash]
    val user = mock[UserInfo]
    val mockedForm = mock[ProfileForm]
    val html = views.html.profile(user, mockedForm.profileInfoForm)(flash, request)
    contentAsString(html) must include("Update")
  }
}
