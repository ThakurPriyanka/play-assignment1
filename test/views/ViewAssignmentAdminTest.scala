package views

import akka.util.Timeout

import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, contentAsString}
import services.DbServiceAssignment

import scala.concurrent.Await
import scala.concurrent.duration._

class ViewAssignmentAdminTest extends PlaySpec with Mockito {

  "Rending view assignment admin page" in new App {
    implicit val duration: Timeout = 5 seconds
    val request = FakeRequest(GET, "/viewAssignment")
    val service = mock[DbServiceAssignment]
    val assignmentList = Await.result(service.getAllAssignment(), Duration.Inf)
    val html = views.html.viewAssignmentAdmin(assignmentList)(request)
    contentAsString(html) must include("Description")
  }
}
