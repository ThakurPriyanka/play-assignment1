
package forms

import play.api.data.Forms._
import play.api.data.Form


case class UserInfoForm(first_name: String, middle_name: String, last_name: String, email: String,
                        pwd: String, confirm_pwd: String, mobile_number: String, gender: String,
                        age: Int, hobbies: String)

class UserForm {
  val minAge = 18
  val maxAge = 75
  val userInfoForm: Form[UserInfoForm] = {
    Form(
      mapping(
        "first_name" -> text.verifying("Please enter the first Name", _.nonEmpty),
        "middle_name" -> text,
        "last_name" -> text.verifying("Please enter the last name", _.nonEmpty),
        "email" -> email,
        "pwd" -> text.verifying("Please enter the password", _.nonEmpty),
        "confirm_pwd" -> text.verifying("Please enter the confirm password", _.nonEmpty),
        "mobile_number" -> text.verifying("A valid phone number is required", field => {
          val regex = """^[789]\d{9}$""".r
          field match {
            case regex() => true
            case _ => false
          }
        }
        ),
        "gender" -> text.verifying("Please select the gender", _.nonEmpty),
        "age" -> number(min = minAge, max = maxAge),
        "hobbies" -> text.verifying("Please select hobbies", _.nonEmpty)
      )(UserInfoForm.apply)(UserInfoForm.unapply)
          verifying(
          "Passwords do not match",
          field => field match {
            case user => {
              if (user.pwd == user.confirm_pwd) {
                true
              } else {
                false
              }
            }
          })
    )
  }
}
