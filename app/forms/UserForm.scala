
package forms
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
      "mobile_number" -> text,
//        .verifying pattern(error="A valid phone number is required", """[0-9.+]+""".r => true ),
      "gender" -> text.verifying("", _.nonEmpty),
      "age" -> number(min = 18, max = 75),
      "hobbies" -> text.verifying("", _.nonEmpty)
        )
      /*verifying (
        "Passwords do not match",
      (user: UserInfoForm) => user.pwd === user.confirm_pwd)*/
        (UserInfoForm.apply)(UserInfoForm.unapply)
  )
}