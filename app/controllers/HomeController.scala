package controllers


import javax.inject._

import play.api.Logger
import models.UserInfo
import play.api.mvc.{AnyContent, Request, _}
import forms.{LoginForm, PasswordForm, ProfileForm, UserForm}
import services.{DbService, DbServiceAssignment}
import utils.PasswordHashing

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class HomeController @Inject()(cc: ControllerComponents, userForm: UserForm, profileForm: ProfileForm,
                               loginForm: LoginForm, passwordForm: PasswordForm, dbService: DbService,
                               dbServiceAssignment: DbServiceAssignment) extends AbstractController(cc) {
  /**
    * @return . go to index page
    */
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  /**
    * @return . go to sign up page
    */
  def goToSignUp(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signUp(userForm.userInfoForm))
  }
  /**
    * @return . after successful sign up go to profile page else not store
    */
  def signUp(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>

    userForm.userInfoForm.bindFromRequest().fold(
      formWithError => {
        Future.successful(BadRequest(views.html.signUp(formWithError)))
      },
      data => {
        val isAdmin = false
        val isEnable = true
        val encryptedPwd = PasswordHashing.encryptPassword(data.pwd)
        val record = UserInfo(0, data.first_name, data.middle_name, data.last_name, data.email,
          encryptedPwd, data.mobile_number, data.gender, data.age, data.hobbies, isAdmin, isEnable)
        dbService.storeInDb(record).map {
          case true
          => Redirect(routes.HomeController.goToProfile).withSession("first_name" -> record.first_name,
            "middle_name" -> record.middle_name, "last_name" -> record.last_name, "email" -> record.email,
            "isAdmin" -> record.isAdmin.toString, "isEnable" -> record.isEnable.toString)
              .flashing("success" -> "user created")
          case false
          => Ok("not stored")
        }
      }
    )
  }

  /**
    * @return . go to profile page if session has email value
    */
  def goToProfile(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val email = request.session.get("email") match {
      case Some(email) => email
      case None => "EmailId does not exists"
    }
    val useResult = dbService.getData(email)
    useResult.map {
      case Some(user)
      => Ok(views.html.profile(user, profileForm.profileInfoForm))
      case None
      => Ok("Something get went wrong")
    }
  }

  /**
    * @return . update profile information for user if session does exit then Ok
    *         else get user detail fill the form
    */
  def profile(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val email = request.session.get("email") match {
      case Some(email) => email
      case None => "EmailId does not exists"
    }
    val useResult = dbService.getData(email)
    useResult.map {
      case Some(user)
      => Ok(views.html.profile(user, profileForm.profileInfoForm))
      case None
      => Ok("Something went wrong")
    }
    profileForm.profileInfoForm.bindFromRequest().fold(
      formWithError => {
        Logger.info(s"${formWithError}")
        useResult.map {
          case Some(user)
          => BadRequest(views.html.profile(user, formWithError))
          case None => Ok("No user")
        }
      },
      data => {
        useResult.flatMap {
          case Some(user)
          => val record = UserInfo(0, data.first_name, data.middle_name, data.last_name, user.email,
            user.pwd, data.mobile_number, data.gender, data.age, data.hobbies, user.isAdmin, user.isEnable)
            dbService.updateInfo(record).map {
              case true
              => Ok("stored")
              case false
              => Ok("not stored")
            }
        }
      }
    )
  }
  /**
    * @return . go to forget password page
    */
  def forgetPassword(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.forgetpassword(passwordForm.passwordInfoForm))
  }
  /**
    * @return . update the password if form is filled correct then OK else if db dont store then ok
    *         if form not filled correctly then badRequest
    */
  def changePassword(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => {
    passwordForm.passwordInfoForm.bindFromRequest().fold(
      formWithError => {
        Future.successful(BadRequest(views.html.forgetpassword(formWithError)))
      },
      data => {
        val encryptedPwd = PasswordHashing.encryptPassword(data.pwd)
        dbService.updatePassword(data.email, encryptedPwd).map {
          case true
          => Ok("done")
          case false
          => Ok("not done")
        }
      }
    )
  }
  }
  /**
    *
    * @return . go to login page
    */
  def goToLogin(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login(loginForm.loginInfoForm))
  }
  /**
    * @return . on right login information go to index page else go to ok
    *         if user is not enable then redirect to login page
    */
  def login(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    loginForm.loginInfoForm.bindFromRequest().fold(
      formWithError => {
        Future.successful(BadRequest(views.html.login(formWithError)))
      },
      data => {
        val encryptedPwd = PasswordHashing.encryptPassword(data.pwd)
        dbService.checkLoginDetail(data.email, encryptedPwd).map {
          case Some(user)
          => {
            if (user.isEnable) {
              Redirect(routes.HomeController.index).withSession("first_name" -> user.first_name,
                "middle_name" -> user.middle_name, "last_name" -> user.last_name, "email" -> user.email,
                "isAdmin" -> user.isAdmin.toString, "isEnable" -> user.isEnable.toString)
                  .flashing("success" -> "user logged in")
            }
            else {
              Redirect(routes.HomeController.goToLogin)
                  .flashing("failure" -> "user can not logged in")
            }
          }
          case None
          => Ok("can not logged in")
        }
      }
    )
  }
  /**
    * @return . show all the list of assignment
    */
  def viewAssignmentUser(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val assignmentResult = dbServiceAssignment.getAllAssignment()
    assignmentResult.map { assignmentList =>
      Ok(views.html.viewAssignmentUser(assignmentList))
    }
  }
  /**
    * @return . clear the session go to index page
    */
  def signOut(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.HomeController.index).withNewSession
  }
}
