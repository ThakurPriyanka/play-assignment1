package views


import akka.util.Timeout
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, contentAsString}
import services.DbService

import scala.concurrent.Await
import scala.concurrent.duration._

class ViewUserTest extends PlaySpec with Mockito {

  "Rending view assignment admin page" in new App {
    implicit val duration: Timeout = 5 seconds
    val request = FakeRequest(GET, "/viewUser")
    val service = mock[DbService]
    val userList = Await.result(service.getUserDetail(), Duration.Inf)
    val html = views.html.viewUser(userList)(request)
    contentAsString(html) must include("Mobile_Number")
  }
}
