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

  def goToAddAssignment(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.addAssignment(assignmentForm.assignmentInfoForm))
  }

    def addAssignment(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
      assignmentForm.assignmentInfoForm.bindFromRequest().fold(
        formWithError => {
          Logger.info(s"${formWithError}")
          Future.successful(BadRequest(views.html.addAssignment(formWithError)))
        },
        data => {
          Logger.info("stored")
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
  def viewUser(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val userListResult = dbService.getUserDetail()
    userListResult.map { userList =>
      Ok(views.html.viewUser(userList))
    }
  }

  def viewAssignment(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val assignmentResult = dbServiceAssignment.getAllAssignment()
    assignmentResult.map { assignmentList =>
       Ok(views.html.viewAssignmentAdmin(assignmentList))
    }
  }

  def deleteAssignment(id: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        dbServiceAssignment.deleteAssignment(id.toInt).map {
          case num if num > 0
          => Ok("deleted")
          case 0
          => Ok("not deleted")
        }
      }

  def enableUser(id: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    dbService.enableUser(id.toInt).map {
      case true
      => Ok("enable")
      case false
      => Ok("can not make it enable")
    }
  }

  def disableUser(id: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    dbService.disableUser(id.toInt).map {
      case true
      => Ok("disable")
      case false
      => Ok("can not make it disable")
    }
  }
}

