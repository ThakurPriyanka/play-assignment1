package controllers

import javax.inject.{Inject, Singleton}
import forms.AssignmentForm
import models.AssignmentInfo
import play.api.Logger
import play.api.mvc._
import services.{DbService, DbServiceAssignment}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
@Singleton
class AdminController @Inject()(cc: ControllerComponents, dbServiceAssignment: DbServiceAssignment,
                                dbService: DbService, assignmentForm: AssignmentForm) extends AbstractController(cc) {
  /**
    *
    * @return . go to add assignment form page
    */
  def goToAddAssignment(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.addAssignment(assignmentForm.assignmentInfoForm))
  }

  /**
    *
    * @return . if form detail is right then  add assignment
    *         else bad request if form is not filled correct
    */
    def addAssignment(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
      assignmentForm.assignmentInfoForm.bindFromRequest().fold(
        formWithError => {
          Future.successful(BadRequest(views.html.addAssignment(formWithError)))
        },
        data => {
          val record = AssignmentInfo(0, data.title, data.description)
          dbServiceAssignment.store(record).map {
            case true
            => Ok("Stored")
            case false
            => Ok("not stored")
          }
        }
      )
    }

  /**
    *
    * @return . go to view user list page
    */
  def viewUser(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val userListResult = dbService.getUserDetail()
    userListResult.map { userList =>
      Ok(views.html.viewUser(userList))
    }
  }

  /**
    *
    * @return . go to view assignment list page
    */
  def viewAssignment(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val assignmentResult = dbServiceAssignment.getAllAssignment()
    assignmentResult.map { assignmentList =>
       Ok(views.html.viewAssignmentAdmin(assignmentList))
    }
  }

  /**
    *
    * @param id . id of assignment that you want to delete
    * @return . after successful deleting Ok
    *         else not delete ok
    */
  def deleteAssignment(id: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        dbServiceAssignment.deleteAssignment(id.toInt).map {
          case num if num > 0
          => Ok("deleted")
          case 0
          => Ok("not deleted")
        }
      }

  /**
    *
    * @param id . id  of the user that admin want to enable user
    * @return . after successful enabling user go to ok
    *         else can not make it enable user
    */
  def enableUser(id: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    dbService.enableUser(id.toInt).map {
      case true
      => Ok("enable")
      case false
      => Ok("can not make it enable")
    }
  }

  /**
    *
    * @param id . id  of the user that admin want to disable user
    * @return . after successful disabling user go to ok
    *         else can not make it disable user
    */
  def disableUser(id: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    dbService.disableUser(id.toInt).map {
      case true
      => Ok("disable")
      case false
      => Ok("can not make it disable")
    }
  }
}

