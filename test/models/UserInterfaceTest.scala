package models

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import services.DbService

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.reflect.ClassTag

class ModelsTest[T: ClassTag] {
  def fakeApp: Application = {
    new GuiceApplicationBuilder()
        .build()
  }

  lazy val app2doo = Application.instanceCache[T]
  lazy val respository: T = app2doo(fakeApp)
}

class UserInterfaceTest extends Specification with Mockito  {

  "User interface test cases" should {
    "store the data"  in {
      val userRepository = new ModelsTest[UserInterface]
      val user = UserInfo(1, "priyanka", "", "thakur", "abc@gmail.com",
        "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
        "9999999999", "female", 23, "Cooking", false, true)
      val actual = Await.result(userRepository.respository.store(user), Duration.Inf)
      actual must equalTo(true)
    }


    "find the data by email" in {
      val userRepository = new ModelsTest[UserInterface]
      val email = "abc@gmail.com"
      val user = Option(UserInfo(1, "priyanka", "", "thakur", "abc@gmail.com",
        "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
        "9999999999", "female", 23, "Cooking", false, true))
      val actual = Await.result(userRepository.respository.findByEmail(email), Duration.Inf)
      actual must equalTo(user)
    }


    "update user info" in {
      val userRepository = new ModelsTest[UserInterface]
      val user = UserInfo(1, "priyanka", "", "thakur", "abc@gmail.com",
        "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
        "9999999999", "female", 23, "Cooking", false, true)

      val actual = Await.result(userRepository.respository.updateUserProfile(user), Duration.Inf)
      actual must equalTo(true)
    }

    "login the user" in {
      val userRepository = new ModelsTest[UserInterface]
      val email = "abc@gmail.com"
      val pwd = "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414"
      val user = Option(UserInfo(1, "priyanka", "", "thakur", "abc@gmail.com",
        "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
        "9999999999", "female", 23, "Cooking", false, true))
      val actual = Await.result(userRepository.respository.checkLogin(email, pwd), Duration.Inf)
      actual must equalTo(user)
    }

    "update password" in {
      val userRepository = new ModelsTest[UserInterface]
      val email = "abc@gmail.com"
      val pwd = "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414"
      val actual = Await.result(userRepository.respository.changePassword(email, pwd), Duration.Inf)
      actual must equalTo(true)
    }

    "get user list" in {
      val userRepository = new ModelsTest[UserInterface]
      val user1 = UserInfo(1, "priyanka", "", "thakur", "abc@gmail.com",
        "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
        "9999999999", "female", 23, "Cooking", false, true)
      val userList = List(user1)
      val actual = Await.result(userRepository.respository.getAllUser(), Duration.Inf)
      actual must equalTo(userList)
    }

    "enable user" in {
      val userRepository = new ModelsTest[UserInterface]
      val id = 1
      val actual = Await.result(userRepository.respository.enable(id), Duration.Inf)
      actual must equalTo(true)
    }

    "disable user" in {
      val userRepository = new ModelsTest[UserInterface]
      val id = 1
      val actual = Await.result(userRepository.respository.disable(id), Duration.Inf)
      actual must equalTo(true)
    }

  }
}