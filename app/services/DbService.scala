package services

import javax.inject.Inject

import models.{UserInfo, UserInterface}
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future

class DbService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                          userInterface: UserInterface) {

  def storeInDb(record: UserInfo): Future[Boolean] = {
    userInterface.store(record)
  }

  def getData(email: String): Future[Option[UserInfo]] = {
    userInterface.findByEmail(email)
  }

  def updateInfo(record: UserInfo): Future[Boolean] = {
    userInterface.updateUserProfile(record)
  }

  def checkLoginDetail(email: String, pwd: String): Future[Option[UserInfo]] = {
    userInterface.checkLogin(email, pwd)
  }

  def updatePassword(email: String, pwd: String): Future[Boolean] = {
    userInterface.changePassword(email, pwd)
  }

  def getUserDetail(): Future[List[UserInfo]] = {
    userInterface.getAllUser()
  }

  def enableUser(id: Int): Future[Boolean] = {
    userInterface.enable(id)
  }

  def disableUser(id: Int): Future[Boolean] = {
    userInterface.disable(id)
  }

}
