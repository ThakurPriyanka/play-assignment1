
package forms

import play.api.Logger
import play.api.data.Forms._
import play.api.data.Form


case class UserInfoForm(first_name: String, middle_name: String, last_name: String, email: String, pwd: String, confirm_pwd: String, mobile_number: String, gender: String, age: Int, hobbies: String)

class UserForm {
  val userInfoForm = Form(
    mapping(
      "first_name" -> text.verifying("", _.nonEmpty),
      "middle_name" -> text,
      "last_name" -> text.verifying("", _.nonEmpty),
      "email" -> email,
      "pwd"-> text.verifying("", _.nonEmpty),
      "confirm_pwd" -> text.verifying("", _.nonEmpty),
      "mobile_number" -> text.verifying( "A valid phone number is required",field => {
        val regex = """^[789]\d{9}$""".r
        field match { case regex() => true
        case _ => false
        }}
      ),
      "gender" -> text.verifying("", _.nonEmpty),
      "age" -> number(min = 18, max = 75),
      "hobbies" -> text.verifying("", _.nonEmpty)
        )(UserInfoForm.apply)(UserInfoForm.unapply)
      verifying(
      "Passwords do not match",
      field => field match {
        case user => {
          if (user.pwd == user.confirm_pwd) { true } else { false }
        }
      })
  )

}