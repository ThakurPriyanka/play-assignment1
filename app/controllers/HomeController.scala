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
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents,  userForm:  UserForm, profileForm: ProfileForm,
                               loginForm: LoginForm, passwordForm: PasswordForm, dbService: DbService,
                               dbServiceAssignment: DbServiceAssignment) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def index()  = Action { implicit request: Request[AnyContent] =>
  Ok(views.html.index())

  }

  def goToSignUp() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signUp(userForm.userInfoForm))
  }

  def signUp() = Action.async { implicit request: Request[AnyContent] =>

    userForm.userInfoForm.bindFromRequest().fold(
      formWithError => {
        //        Future.successful(BadRequest(views.html.error1(formWithError)))
        Logger.info(s"${formWithError}")
        Future.successful(BadRequest(views.html.signUp(formWithError)))
      },
      data => {
        Logger.info("stored")
        val isAdmin = false
        val isEnable = true
        val encryptedPwd = PasswordHashing.encryptPassword(data.pwd)
        val record = UserInfo(0, data.first_name, data.middle_name, data.last_name, data.email,
          encryptedPwd, data.mobile_number, data.gender, data.age, data.hobbies, isAdmin, isEnable)
        dbService.storeInDb(record).map {
          case true
          => Redirect(routes.HomeController.goTo).withSession( "first_name" -> record.first_name,
            "middle_name" -> record.middle_name, "last_name" -> record.last_name, "email"-> record.email,
          "isAdmin" -> record.isAdmin.toString, "isEnable" -> record.isEnable.toString)
            .flashing("success"-> "user created")
          case false
          => Ok("not stored")
        }
      }
    )
  }

  def goTo() = Action.async { implicit request: Request[AnyContent] =>
    //  Ok(views.html.index())
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
  def profile() = Action.async { implicit request: Request[AnyContent] =>
    Logger.info("entered")
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
        Logger.info("stored")
     useResult.flatMap {
       case Some(user)
       => val record = UserInfo(0, data.first_name, data.middle_name, data.last_name, user.email,
         user.pwd, data.mobile_number, data.gender, data.age, data.hobbies, user.isAdmin, user.isEnable)
         Logger.info(record.first_name)
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

  def forgetPassword() = Action {implicit request: Request[AnyContent] =>
    Ok(views.html.forgetpassword(passwordForm.passwordInfoForm))
  }

  def changePassword()= Action.async  { implicit request: Request[AnyContent] => {
    passwordForm.passwordInfoForm.bindFromRequest().fold(
      formWithError => {
        Logger.info(s"${formWithError}")
        Future.successful(BadRequest(views.html.forgetpassword(formWithError)))
      },
      data => {
        Logger.info("stored")
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


  def goToLogin() = Action  { implicit request: Request[AnyContent] =>
      Ok(views.html.login(loginForm.loginInfoForm))
  }

  def login() = Action.async  { implicit request: Request[AnyContent] =>
    loginForm.loginInfoForm.bindFromRequest().fold(
      formWithError => {
        Logger.info(s"${formWithError}")
        Future.successful(BadRequest(views.html.login(formWithError)))
      },
      data => {
        Logger.info("stored")
        val encryptedPwd = PasswordHashing.encryptPassword(data.pwd)
            dbService.checkLoginDetail(data.email, encryptedPwd).map {
              case Some(user)
              =>  {if(user.isEnable) {
                Redirect(routes.HomeController.index).withSession("first_name" -> user.first_name,
                  "middle_name" -> user.middle_name, "last_name" -> user.last_name, "email" -> user.email,
                  "isAdmin" -> user.isAdmin.toString, "isEnable" -> user.isEnable.toString)
                  .flashing("success" -> "user logged in")
              }
              else {
                Redirect(routes.HomeController.goToLogin)
                  .flashing("failure" -> "user can not logged in")
              }}
              case None
              => Ok("can not logged in")
            }
        }
    )
  }

  def viewAssignmentUser() = Action.async { implicit request: Request[AnyContent] =>
    val assignmentResult = dbServiceAssignment.getAllAssignment()
    assignmentResult.map { assignmentList =>
      Ok(views.html.viewAssignmentUser(assignmentList))
    }
  }
  def signOut()= Action { implicit request: Request[AnyContent] =>
    Logger.info("out")
    Ok("bye").withNewSession
  }
}
