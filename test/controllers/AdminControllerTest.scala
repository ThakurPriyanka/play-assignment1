package controllers


import forms._
import models.{AssignmentInfo, UserInfo}
import org.mockito.Mockito.when
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.Helpers.stubControllerComponents
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._
import services.{DbService, DbServiceAssignment}


import scala.concurrent.Future

class AdminControllerTest extends PlaySpec with Mockito {

  "go to add assignment page" in {
    val controller = getMockedObject
    val result = controller.adminController.goToAddAssignment().apply(FakeRequest()
      .withCSRFToken)
    status(result) must equal(OK)
  }

  "show all assignment" in {
    val controller = getMockedObject
    val assignment1 = AssignmentInfo(0,"assignment1", "make an app of play")
    val assignment2 = AssignmentInfo(0,"assignment1", "make an app of play")
    val assignmentList = List(assignment1, assignment2)

    when(controller.dbServiceAssignment.getAllAssignment()) thenReturn Future.successful(assignmentList)
    val result = controller.adminController.viewAssignment().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  "show all user" in {
    val controller = getMockedObject
    val user1 = UserInfo(0, "priyanka", "", "thakur", "abc@gmail.com",
      "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
      "9999999999", "female", 23, "Cooking", false, true)
    val user2 = UserInfo(0, "priyanka", "", "thakur", "abc@gmail.com",
      "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
      "9999999999", "female", 23, "Cooking", false, true)
    val userList = List(user1, user2)

    when(controller.dbService.getUserDetail()) thenReturn Future.successful(userList)
    val result = controller.adminController.viewUser().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  "should store a assignment" in {
    val controller = getMockedObject

    val assignment = AssignmentInfoForm("assignment1", "play application")

    val assignmentForm = new AssignmentForm {}.assignmentInfoForm.fill(assignment)
    val payload = models.AssignmentInfo(0,"assignment1", "play application")


    when(controller.assignmentForm.assignmentInfoForm) thenReturn assignmentForm
    when(controller.dbServiceAssignment.store(payload)) thenReturn Future.successful(true)


    val request = FakeRequest(POST, "/signUp").withFormUrlEncodedBody("csrfToken"
      -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea",
      "title" -> "assignment1", "description"->"play application")
      .withCSRFToken

    val result = controller.adminController.addAssignment().apply(request)
    status(result) must equal(OK)
  }

  "delete assignment" in {
    val controller = getMockedObject
    val id = 1
    val resultNumber = 1
    when(controller.dbServiceAssignment.deleteAssignment(id)) thenReturn Future.successful(resultNumber)
    val result = controller.adminController.deleteAssignment(id.toString).apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  "enable user" in {
    val controller = getMockedObject
    val id = 1
    when(controller.dbService.enableUser(id)) thenReturn Future.successful(true)
    val result = controller.adminController.enableUser(id.toString).apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  "disable user" in {
    val controller = getMockedObject
    val id = 1
    when(controller.dbService.disableUser(id)) thenReturn Future.successful(true)
    val result = controller.adminController.disableUser(id.toString).apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  def getMockedObject: TestObjects = {
    val mockedAssignmentFormRepo = mock[AssignmentForm]
    val dbService = mock[DbService]
    val dbServiceAssignment = mock[DbServiceAssignment]


    val controller = new AdminController(stubControllerComponents(), dbServiceAssignment,
      dbService, mockedAssignmentFormRepo)

    TestObjects(stubControllerComponents(),  dbServiceAssignment, dbService, mockedAssignmentFormRepo, controller)
  }

  case class TestObjects(controllerComponent: ControllerComponents,
                         dbServiceAssignment: DbServiceAssignment,
                         dbService: DbService,
                         assignmentForm: AssignmentForm,
                         adminController: AdminController)

}

