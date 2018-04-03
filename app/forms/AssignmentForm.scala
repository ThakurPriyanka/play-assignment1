package forms

import play.api.data.Form
import play.api.data.Forms.{mapping, text}

case class AssignmentInfoForm(title: String, description: String)

class AssignmentForm {

  val assignmentInfoForm: Form[AssignmentInfoForm] = {
    Form(
      mapping(
        "title" -> text.verifying("Please enter the title",title =>  title.nonEmpty),
        "description" -> text.verifying("Please enter the description",description =>  description.nonEmpty)
      )
      (AssignmentInfoForm.apply)(AssignmentInfoForm.unapply)
    )
  }
}
