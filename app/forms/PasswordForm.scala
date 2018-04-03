package forms


import play.api.data.Form
import play.api.data.Forms._


  case class PasswordInfoForm(email: String, pwd: String, confirm_pwd: String)

  class PasswordForm  {

    val passwordInfoForm: Form[PasswordInfoForm] = {
      Form(
        mapping(
          "email" -> email.verifying("Please enter the email", _.nonEmpty),
          "pwd" -> text.verifying("Please enter the password", _.nonEmpty),
          "confirm_pwd" -> text.verifying("Please enter the confirm password", _.nonEmpty)
        )
        (PasswordInfoForm.apply)(PasswordInfoForm.unapply) verifying(
            "Passwords do not match",
            field => field match {
              case user => user.pwd == user.confirm_pwd
            })
      )
    }
}
