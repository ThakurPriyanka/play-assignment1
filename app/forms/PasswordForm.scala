package forms


import play.api.data.Form
import play.api.data.Forms._


  case class PasswordInfoForm(email: String, pwd: String, confirm_pwd: String)

  class PasswordForm  {

    val passwordInfoForm = Form(
      mapping(
        "email" -> email,
        "pwd"-> text.verifying("", _.nonEmpty),
        "confirm_pwd"-> text.verifying("", _.nonEmpty),
      )
      (PasswordInfoForm.apply)(PasswordInfoForm.unapply) verifying(
        "Passwords do not match",
        field => field match { case user => user.pwd == user.confirm_pwd})
    )
}
