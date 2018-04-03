package services

import javax.inject.Inject

import models.{UserInfo, UserInterface}
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future

class DbService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                          userInterface: UserInterface) {
  /**
    *
    * @param record . information of user you want to store
    * @return . true or false on successful storage or not
    */
  def storeInDb(record: UserInfo): Future[Boolean] = {
    userInterface.store(record)
  }

  /**
    *
    * @param email . of user whose information want to get
    * @return . option of user information
    */
  def getData(email: String): Future[Option[UserInfo]] = {
    userInterface.findByEmail(email)
  }
  /**
    *
    * @param record . information of user you want to update
    * @return . true or false on successful update or not
    */
  def updateInfo(record: UserInfo): Future[Boolean] = {
    userInterface.updateUserProfile(record)
  }

  /**
    *
    * @param email . of user who want to login
    * @param pwd . of user who want to login
    * @return . option of user information according to email and password
    */
  def checkLoginDetail(email: String, pwd: String): Future[Option[UserInfo]] = {
    userInterface.checkLogin(email, pwd)
  }
  /**
    *
    * @param email . of user who want to change password
    * @param pwd . of user new password
    * @return . true false on successful updation of password or not
    */
  def updatePassword(email: String, pwd: String): Future[Boolean] = {
    userInterface.changePassword(email, pwd)
  }
  /**
    *
    * @return . list of user Information
    */
  def getUserDetail(): Future[List[UserInfo]] = {
    userInterface.getAllUser()
  }
  /**
    *
    * @param id . of user to make that user enable
    * @return . true or false on successful enabling or not
    */
  def enableUser(id: Int): Future[Boolean] = {
    userInterface.enable(id)
  }


  /**
    *
    * @param id . of user to make that user disable
    * @return . true or false on successful disabling or not
    */
  def disableUser(id: Int): Future[Boolean] = {
    userInterface.disable(id)
  }

}
