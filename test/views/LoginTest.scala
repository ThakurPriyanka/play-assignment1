package views

import akka.util.Timeout
import forms.LoginForm
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.Flash
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, contentAsString}
import scala.concurrent.duration._

  class LoginTest extends PlaySpec with Mockito {
  "Rending add login page" in new App {
    implicit val duration: Timeout = 5 seconds
    val request = FakeRequest(POST, "/login")
    val flash = mock[Flash]
    val mockedForm = mock[LoginForm]
    val html = views.html.login(mockedForm.loginInfoForm)(flash, request)
    contentAsString(html) must include("login")
  }
}
