package controllers

import javax.inject.{Inject, Singleton}

import forms.AssignmentForm
import models.AssignmentInfo
import play.api.Logger
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.DbServiceAssignment
import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.Future
@Singleton
class AdminController @Inject()(cc: ControllerComponents, dbServiceAssignment: DbServiceAssignment,
                                assignmentForm: AssignmentForm) extends AbstractController(cc) {

  def goToAddAssignment() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.addAssignment(assignmentForm.assignmentInfoForm))
  }

    def addAssignment() = Action.async { implicit request: Request[AnyContent] =>
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

  def viewAssignment() = Action.async { implicit request: Request[AnyContent] =>
    val assignmentResult = dbServiceAssignment.getAllAssignment()
    assignmentResult.map { assignmentList =>
       Ok(views.html.viewAssignmentAdmin(assignmentList))
    }
  }

  def deleteAssignment(id: String) = Action.async { implicit request: Request[AnyContent] =>
        dbServiceAssignment.deleteAssignment(id.toInt).map {
          case num if num > 0
          => Ok("deleted")
          case 0
          => Ok("not deleted")
        }
      }
  }

