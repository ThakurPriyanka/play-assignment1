package forms

import play.api.data.Form
import play.api.data.Forms._

case class LoginInfoForm(email: String, pwd: String)

class LoginForm {

  val loginInfoForm: Form[LoginInfoForm] = {
    Form(
      mapping(
        "email" -> email,
        "pwd" -> text.verifying("", _.nonEmpty),
      )
      (LoginInfoForm.apply)(LoginInfoForm.unapply)
    )
  }
}
