package forms

import play.api.data.Forms._
import play.api.data.Form


case class ProfileInfoForm(first_name: String, middle_name: String, last_name: String, mobile_number: String, gender: String, age: Int, hobbies: String)

class ProfileForm {

  val profileInfoForm = Form(
    mapping(
      "first_name" -> text.verifying("", _.nonEmpty),
      "middle_name" -> text,
      "last_name" -> text.verifying("", _.nonEmpty),
      "mobile_number" -> text,
      //        .verifying pattern(error="A valid phone number is required", """[0-9.+]+""".r => true ),
      "gender" -> text.verifying("", _.nonEmpty),
      "age" -> number(min = 18, max = 75),
      "hobbies" -> text.verifying("", _.nonEmpty)
    )
      /*verifying (
        "Passwords do not match",
      (user: UserInfoForm) => user.pwd === user.confirm_pwd)*/
      (ProfileInfoForm.apply)(ProfileInfoForm.unapply)
  )
}
