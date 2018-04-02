package views



import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, _}

class IndexTest extends PlaySpec with Mockito {

  "Rending index page" in new App {

    val request = FakeRequest(POST, "/")
    val html = views.html.index()(request)
    contentAsString(html) must include("Welcome to Play")
  }

}
