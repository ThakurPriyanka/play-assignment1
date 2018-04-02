package forms

import play.api.data.Forms._
import play.api.data.Form


case class ProfileInfoForm(first_name: String, middle_name: String, last_name: String, mobile_number: String, gender: String, age: Int, hobbies: String)

class ProfileForm {
  val minAge = 18
  val maxAge = 75
  val profileInfoForm: Form[ProfileInfoForm] = {
    Form(
      mapping(
        "first_name" -> text.verifying("", _.nonEmpty),
        "middle_name" -> text,
        "last_name" -> text.verifying("", _.nonEmpty),
        "mobile_number" -> text.verifying("A valid phone number is required", field => {
          val regex = """[0-9.+]+""".r
          field match {
            case regex(_) => true
            case _ => false
          }
        }
        ),
        //
        "gender" -> text.verifying("", _.nonEmpty),
        "age" -> number(min = minAge, max = maxAge),
        "hobbies" -> text.verifying("", _.nonEmpty)
      )(ProfileInfoForm.apply)(ProfileInfoForm.unapply)
    )
  }
}
