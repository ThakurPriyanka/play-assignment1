package forms

import play.api.data.Form
import play.api.data.Forms.{mapping, text}

case class AssignmentInfoForm(title: String, description: String)

class AssignmentForm {

  val assignmentInfoForm: Form[AssignmentInfoForm] = {
    Form(
      mapping(
        "title" -> text.verifying("", _.nonEmpty),
        "description" -> text.verifying("", _.nonEmpty),
      )
      (AssignmentInfoForm.apply)(AssignmentInfoForm.unapply)
    )
  }
}
