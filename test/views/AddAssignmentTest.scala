package views

import forms.AssignmentForm
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, _}

class AddAssignmentTest extends PlaySpec with Mockito {
  "Rending add assignment page" in new App {
    val request = FakeRequest(POST, "/addAssignment")
    val mockedForm = mock[AssignmentForm]
    val html = views.html.addAssignment(mockedForm.assignmentInfoForm)(request)
    contentAsString(html) must include("Description")
  }

}