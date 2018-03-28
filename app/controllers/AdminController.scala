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
  }

