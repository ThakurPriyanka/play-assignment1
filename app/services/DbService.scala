package services

import javax.inject.Inject

import forms.UserForm
import models.{UserInfo, UserInterface}
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.ControllerComponents

import scala.concurrent.Future

class DbService @Inject()(protected  val dbConfigProvider:DatabaseConfigProvider,userInterface: UserInterface){
  def storeInDb(record: UserInfo): Future[Boolean] = {
    userInterface.store(record)
  }

  def getData(email: String): Future[Option[UserInfo]] = {
    userInterface.findByEmail(email)
  }

  def updateInfo(record: UserInfo): Future[Boolean] = {
    userInterface.updateUserProfile(record)
  }

  def checkLoginDetail(email: String, pwd: String): Future[Boolean] = {
    userInterface.checkLogin(email, pwd)
  }
}
