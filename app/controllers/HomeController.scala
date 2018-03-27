package controllers


import javax.inject._

import play.api.Logger
import models.{UserInfo, UserInterface}
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{AnyContent, Request, _}
import forms.{LoginForm, ProfileForm, UserForm}
import services.DbService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents,  userForm:  UserForm, profileForm: ProfileForm, loginForm: LoginForm, dbService: DbService) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def index()  = Action { implicit request: Request[AnyContent] =>
//  Ok(views.html.index())
  Ok(views.html.signUp(userForm.userInfoForm))
  }

  def signUp() = Action.async { implicit request: Request[AnyContent] =>
    Logger.info("entered")
    userForm.userInfoForm.bindFromRequest().fold(
      formWithError => {
        //        Future.successful(BadRequest(views.html.error1(formWithError)))
        Logger.info(s"${formWithError}")
        Future.successful(BadRequest(views.html.signUp(formWithError)))
      },
      data => {
        Logger.info("stored")
        val record = UserInfo(0, data.first_name, data.middle_name, data.last_name, data.email,
          data.pwd, data.mobile_number, data.gender, data.age, data.hobbies, false, true)
        dbService.storeInDb(record).map {
          case true
          => Redirect(routes.HomeController.goTo).withSession( "first_name" -> record.first_name,
            "middle_name" -> record.middle_name, "last_name" -> record.last_name, "email"-> record.email)
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
      => Ok("Something get went wrong")
    }
    profileForm.profileInfoForm.bindFromRequest().fold(
      formWithError => {
        //        Future.successful(BadRequest(views.html.error1(formWithError)))
        Logger.info(s"${formWithError}")
        useResult.map {
          case Some(user)
          => BadRequest(views.html.profile(user, formWithError))
        }
      },
      data => {
        Logger.info("stored")
     useResult.flatMap {
          case Some(user)
          =>  val record = UserInfo(0, data.first_name, data.middle_name, data.last_name, user.email,
            user.pwd, data.mobile_number, data.gender, data.age, data.hobbies, user.isAdmin, user.isEnable)
            Logger.info(record.first_name);dbService.updateInfo(record).map {
            case true
            => Ok("stored")
            case false
            => Ok("not stored")
          }
        }
      }
    )
  }

  def goToLogin() = Action  { implicit request: Request[AnyContent] => {
      Ok(views.html.login)
  }

  def login() = Action.async  { implicit request: Request[AnyContent] =>
    loginForm.loginInfoForm.bindFromRequest().fold(
      formWithError => {
        Logger.info(s"${formWithError}")
        Future.successful(BadRequest(views.html.login(formWithError)))
      },
      data => {
        Logger.info("stored")
            dbService.checkLoginDetail(data.email, data.pwd).map {
              case true
              => Ok("login")
              case false
              => Ok("not login")
            }
        }
    )
  }

  def signOut()= Action { implicit request: Request[AnyContent] =>
      Ok("bye").withNewSession
  }
}
