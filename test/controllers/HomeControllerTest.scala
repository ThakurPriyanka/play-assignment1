package controllers


import forms._
import models.AssignmentInfo
import org.mockito.Mockito.when
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.{ControllerComponents, Session}
import play.api.test.FakeRequest
import play.api.test.Helpers.stubControllerComponents
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._
import services.{DbService, DbServiceAssignment}
import utils.PasswordHashing

import scala.concurrent.Future

class HomeControllerTest extends PlaySpec with Mockito {

  "go to index page" in {
    val controller = getMockedObject
    val result = controller.homeController.index().apply(FakeRequest())
    status(result) must equal(OK)
  }

  "go to user info page" in {
    val controller = getMockedObject
    when(controller.userForm.userInfoForm) thenReturn { val form = new UserForm{}
      form.userInfoForm}
    val result = controller.homeController.goToSignUp().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  "go to password form page" in {
    val controller = getMockedObject
    when(controller.passwordForm.passwordInfoForm) thenReturn { val form = new PasswordForm{}
      form.passwordInfoForm}
    val result = controller.homeController.forgetPassword().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  "go to login page" in {
    val controller = getMockedObject
    when(controller.loginForm.loginInfoForm) thenReturn { val form = new LoginForm{}
      form.loginInfoForm}
    val result = controller.homeController.goToLogin().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  "go to login profile page" in {
    val controller = getMockedObject
    mock[Session]
    mock[ProfileForm]
    val email = "abc@gmail.com"
    val user = Option(models.UserInfo(0,"priyanka", "", "thakur", "abc@gmail.com",
      "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea",
      "9999999999", "female", 23, "Cooking", false, true))
    when(controller.dbService.getData(email)) thenReturn Future.successful(user)
    when(controller.profileForm.profileInfoForm) thenReturn { val form = new ProfileForm{}
      form.profileInfoForm}
    val result = controller.homeController.goToProfile()
      .apply(FakeRequest().withSession("email" -> "abc@gmail.com").withCSRFToken)
    status(result) must equal(OK)
  }

  "should store a user" in {
    val controller = getMockedObject

    val user = UserInfoForm("priyanka", "", "thakur", "abc@gmail.com",
     "qaz","qaz",
      "9999999999", "female", 23, "Cooking")

    val userForm = new UserForm {}.userInfoForm.fill(user)
    val encryptedpwd = PasswordHashing.encryptPassword(user.pwd)
    val payload = models.UserInfo(0,"priyanka", "", "thakur", "abc@gmail.com",
      encryptedpwd,
      "9999999999", "female", 23, "Cooking", false, true)


    when(controller.userForm.userInfoForm) thenReturn userForm
    when(controller.dbService.storeInDb(payload)) thenReturn Future.successful(true)


    val request = FakeRequest(POST, "/signUp").withFormUrlEncodedBody("csrfToken"
      -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea", "first_name"-> "priyanka","middle_name" -> "", "last_name" -> "thakur","email"-> "abc@gmail.com",
     "pwd"-> "qaz","confirm_pwd"-> "qaz",
      "mobile_number" -> "9999999999","gender"-> "female", "age" -> "23","hobbies"-> "Cooking")
      .withCSRFToken

    val result = controller.homeController.signUp().apply(request)
    status(result) must equal(303)
  }

  "user should login" in {
    val controller = getMockedObject

    val login = LoginInfoForm("abc@gmail.com", "qaz")

    val loginForm = new LoginForm {}.loginInfoForm.fill(login)
    val encryptedpwd = PasswordHashing.encryptPassword(login.pwd)
    val user = Option(models.UserInfo(0,"priyanka", "", "thakur", "abc@gmail.com",
      "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea",
      "9999999999", "female", 23, "Cooking", false, true))

    when(controller.loginForm.loginInfoForm) thenReturn loginForm
    when(controller.dbService.checkLoginDetail(login.email,encryptedpwd)) thenReturn Future.successful(user)


    val request = FakeRequest(POST, "/login").withFormUrlEncodedBody("csrfToken"
      -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea", "email"-> "abc@gmail.com",
      "pwd"-> "qaz")
      .withCSRFToken

    val result = controller.homeController.login().apply(request)
    status(result) must equal(303)
  }

  "show all user assignment" in {
    val controller = getMockedObject
    val assignment1 = AssignmentInfo(0,"assignment1", "make an app of play")
    val assignment2 = AssignmentInfo(0,"assignment1", "make an app of play")
    val assignmentList = List(assignment1, assignment2)

    when(controller.dbServiceAssignment.getAllAssignment()) thenReturn Future.successful(assignmentList)
    val result = controller.homeController.viewAssignmentUser().apply(FakeRequest().withCSRFToken)
    status(result) must equal(OK)
  }

  "user changed password" in {
    val controller = getMockedObject

    val password = PasswordInfoForm( "abc@gmail.com",
      "qaz","qaz")

    val passwordForm = new PasswordForm {}.passwordInfoForm.fill(password)
    val encryptedpwd = PasswordHashing.encryptPassword(password.pwd)

    when(controller.passwordForm.passwordInfoForm) thenReturn passwordForm
    when(controller.dbService.updatePassword(password.email, encryptedpwd)) thenReturn Future.successful(true)

    val request = FakeRequest(POST, "/changePassword").withFormUrlEncodedBody("csrfToken"
      -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea","email"-> "abc@gmail.com",
      "pwd"-> "qaz","confirm_pwd"-> "qaz")
      .withCSRFToken

    val result = controller.homeController.changePassword().apply(request)
    status(result) must equal(OK)

  }

  "user should logout" in {
    val controller = getMockedObject
    val result = controller.homeController.signOut().apply(FakeRequest().withSession())
    status(result) must equal(303)
  }
  "should update user" in {
    val controller = getMockedObject

    val user = UserInfoForm("priyanka", "", "thakur", "abc@gmail.com",
      "qaz","qaz",
      "9999999999", "female", 23, "Cooking")

    val userForm = new UserForm {}.userInfoForm.fill(user)
    val encryptedpwd = PasswordHashing.encryptPassword(user.pwd)
    val payload = models.UserInfo(0,"priyanka", "", "thakur", "abc@gmail.com",
      encryptedpwd,
      "9999999999", "female", 23, "Cooking", false, true)


    when(controller.userForm.userInfoForm) thenReturn userForm
    when(controller.dbService.storeInDb(payload)) thenReturn Future.successful(true)


    val request = FakeRequest(POST, "/signUp").withFormUrlEncodedBody("csrfToken"
      -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea", "first_name"-> "priyanka","middle_name" -> "", "last_name" -> "thakur","email"-> "abc@gmail.com",
      "pwd"-> "qaz","confirm_pwd"-> "qaz",
      "mobile_number" -> "9999999999","gender"-> "female", "age" -> "23","hobbies"-> "Cooking")
      .withCSRFToken

    val result = controller.homeController.signUp().apply(request)
    status(result) must equal(303)
  }


  def getMockedObject: TestObjects = {
    val mockedUserFormRepo = mock[UserForm]
    val mockedProfileForm = mock[ProfileForm]
    val mockedLoginForm = mock[LoginForm]
    val mockedPasswordForm = mock[PasswordForm]
    val dbService = mock[DbService]
    val dbServiceAssignment = mock[DbServiceAssignment]



    val controller = new HomeController(stubControllerComponents(), mockedUserFormRepo, mockedProfileForm,
      mockedLoginForm, mockedPasswordForm, dbService, dbServiceAssignment)

    TestObjects(stubControllerComponents(), mockedUserFormRepo, mockedProfileForm,
      mockedLoginForm, mockedPasswordForm, dbService, dbServiceAssignment, controller)
  }

  case class TestObjects(controllerComponent: ControllerComponents,
                         userForm: UserForm,
                         profileForm: ProfileForm,
                         loginForm: LoginForm,
                         passwordForm: PasswordForm,
                         dbService: DbService,
                         dbServiceAssignment: DbServiceAssignment,
                         homeController: HomeController)

}
