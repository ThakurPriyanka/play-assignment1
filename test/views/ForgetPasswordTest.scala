package views

import forms.PasswordForm
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, contentAsString}
import akka.util.Timeout
import scala.concurrent.duration._

class ForgetPasswordTest extends PlaySpec with Mockito {

    "Rending forget password page" in new App {
      implicit val duration: Timeout = 5 seconds
    val request = FakeRequest(GET, "/forgetPassword")
    val mockedForm = mock[PasswordForm]
    val html = views.html.forgetpassword(mockedForm.passwordInfoForm)(request)
    contentAsString(html) must include("change Password")
  }

}
