package forms

import play.api.data.Forms._
import play.api.data.Form


case class ProfileInfoForm(first_name: String, middle_name: String, last_name: String,
                           mobile_number: String, gender: String, age: Int, hobbies: String)

class ProfileForm {
  val minAge = 18
  val maxAge = 75
  val profileInfoForm: Form[ProfileInfoForm] = {
    Form(
      mapping(
        "first_name" -> text.verifying("add first name", _.nonEmpty),
        "middle_name" -> text,
        "last_name" -> text.verifying("add last name", _.nonEmpty),
        "mobile_number" -> text.verifying("A valid phone number is required", field => {
          val regex = """[0-9.+]+""".r
          field match {
            case regex() => true
          }
        }
        ),
        //
        "gender" -> text.verifying("add gender", _.nonEmpty),
        "age" -> number(min = minAge, max = maxAge),
        "hobbies" -> text.verifying("select hobbies", _.nonEmpty)
      )(ProfileInfoForm.apply)(ProfileInfoForm.unapply)
    )
  }
}
