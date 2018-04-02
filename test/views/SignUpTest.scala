package views

import akka.util.Timeout
import forms.UserForm
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, contentAsString}

import scala.concurrent.duration._

class SignUpTest extends PlaySpec with Mockito {
  "Rending add sign up page" in new App {
    implicit val duration: Timeout = 5 seconds
    val request = FakeRequest(GET, "/signUpForm")
    val mockedForm = mock[UserForm]
    val html = views.html.signUp(mockedForm.userInfoForm)(request)
    contentAsString(html) must include("submit")
  }
}