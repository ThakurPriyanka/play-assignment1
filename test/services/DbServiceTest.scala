package services

import models.{UserInfo, UserInterface}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


class DbServiceTest extends Specification with Mockito {

  "store the data" in {
    val userRepository = mock[UserInterface]
    val user = UserInfo(0, "priyanka", "", "thakur", "abc@gmail.com",
      "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
      "9999999999", "female", 23, "Cooking", false, true)
    userRepository.store(user) returns Future.successful(true)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val userService = new DbService(db, userRepository)
    val actual = Await.result(userService.storeInDb(user), Duration.Inf)
    actual must equalTo(true)
  }

  "find the data by email" in {
    val userRepository = mock[UserInterface]
    val email = "abc@gmail.com"
    val user = Option(UserInfo(0, "priyanka", "", "thakur", "abc@gmail.com",
      "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
      "9999999999", "female", 23, "Cooking", false, true))
    userRepository.findByEmail(email) returns Future.successful(user)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val userService = new DbService(db, userRepository)
    val actual = Await.result(userService.getData(email), Duration.Inf)
    actual must equalTo(user)
  }


  "update user info" in {
    val userRepository = mock[UserInterface]
    val user = UserInfo(0, "priyanka", "", "thakur", "abc@gmail.com",
      "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
      "9999999999", "female", 23, "Cooking", false, true)
    userRepository.updateUserProfile(user) returns Future.successful(true)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val userService = new DbService(db, userRepository)
    val actual = Await.result(userService.updateInfo(user), Duration.Inf)
    actual must equalTo(true)
  }

  "login the user" in {
    val userRepository = mock[UserInterface]
    val email = "abc@gmail.com"
    val pwd = "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414"
    val user = Option(UserInfo(0, "priyanka", "", "thakur", "abc@gmail.com",
      "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
      "9999999999", "female", 23, "Cooking", false, true))
    userRepository.checkLogin(email, pwd) returns Future.successful(user)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val userService = new DbService(db, userRepository)
    val actual = Await.result(userService.checkLoginDetail(email, pwd), Duration.Inf)
    actual must equalTo(user)
  }

  "update password" in {
    val userRepository = mock[UserInterface]
    val email = "abc@gmail.com"
    val pwd = "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414"
    userRepository.changePassword(email, pwd) returns Future.successful(true)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val userService = new DbService(db, userRepository)
    val actual = Await.result(userService.updatePassword(email, pwd), Duration.Inf)
    actual must equalTo(true)
  }

  "get user list" in {
    val userRepository = mock[UserInterface]
    val user1 = UserInfo(0, "priyanka", "", "thakur", "abc@gmail.com",
      "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
      "9999999999", "female", 23, "Cooking", false, true)
    val user2 = UserInfo(0, "priyanka", "", "thakur", "abc@gmail.com",
      "8d6c5597d25eca212ea6c6cacc00e247b8c631343a70147cb81374ff72f414",
      "9999999999", "female", 23, "Cooking", false, true)
    val userList = List(user1, user2)
    userRepository.getAllUser() returns Future.successful(userList)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val userService = new DbService(db, userRepository)
    val actual = Await.result(userService.getUserDetail(), Duration.Inf)
    actual must equalTo(userList)
  }

  "enable user" in {
    val userRepository = mock[UserInterface]
    val id = 1
    userRepository.enable(id) returns Future.successful(true)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val userService = new DbService(db, userRepository)
    val actual = Await.result(userService.enableUser(id), Duration.Inf)
    actual must equalTo(true)
  }

  "disable user" in {
    val userRepository = mock[UserInterface]
    val id = 1
    userRepository.disable(id) returns Future.successful(true)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val userService = new DbService(db, userRepository)
    val actual = Await.result(userService.disableUser(id), Duration.Inf)
    actual must equalTo(true)
  }

}
