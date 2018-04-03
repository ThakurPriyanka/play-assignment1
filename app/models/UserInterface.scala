package models

import javax.inject.Inject


import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class UserInfo(id: Int, first_name: String, middle_name: String, last_name: String, email: String, pwd: String,
                    mobile_number: String, gender: String, age: Int, hobbies: String, isAdmin: Boolean, isEnable: Boolean)


class UserInterface @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
    extends UserProfileBaseRepository
        with UserProfileBaseRepositoryImpl with UserProfileRepositoryTable


trait UserProfileBaseRepository {
  def store(userProfile: UserInfo): Future[Boolean]

  def findByEmail(email: String): Future[Option[UserInfo]]

  def updateUserProfile(user: UserInfo): Future[Boolean]

  def checkLogin(email: String, pwd: String): Future[Option[UserInfo]]

  def changePassword(email: String, pwd: String): Future[Boolean]

  def getAllUser(): Future[List[UserInfo]]

  def enable(id: Int): Future[Boolean]

  def disable(id: Int): Future[Boolean]
}


trait UserProfileBaseRepositoryImpl extends UserProfileBaseRepository {
  self: UserProfileRepositoryTable =>

  import profile.api._

  /**
    *
    * @param userProfile . information of user you want to store
    * @return . true or false on successful storage or not
    */
  def store(userProfile: UserInfo): Future[Boolean] =
    db.run(userProfileQuery += userProfile) map (_ > 0)

  /**
    *
    * @param email . of user whose information want to get
    * @return . option of user information
    */
  def findByEmail(email: String): Future[Option[UserInfo]] = {
    val queryResult = userProfileQuery.filter(_.email.toLowerCase === email.toLowerCase).result.headOption
    db.run(queryResult)
  }

  /**
    *
    * @param userInfo . information of user you want to update
    * @return . true or false on successful update or not
    */
  def updateUserProfile(userInfo: UserInfo): Future[Boolean] = {
    db.run(userProfileQuery.filter(_.email.toLowerCase === userInfo.email.toLowerCase)
        .map(user => (user.first_name, user.middle_name, user.last_name, user.mobile_number, user.gender, user.age, user.hobbies))
        .update(userInfo.first_name, userInfo.middle_name, userInfo.last_name, userInfo.mobile_number,
          userInfo.gender, userInfo.age, userInfo.hobbies)) map (_ > 0)
  }

  /**
    *
    * @param email . of user who want to login
    * @param pwd . of user who want to login
    * @return . option of user information according to email and password
    */
  def checkLogin(email: String, pwd: String): Future[Option[UserInfo]] = {
    val queryResult = userProfileQuery.filter(user => user.email.toLowerCase === email.toLowerCase && user.pwd === pwd).result.headOption
    db.run(queryResult)
  }

  /**
    *
    * @param email . of user who want to change password
    * @param pwd . of user new password
    * @return . true false on successful updation of password or not
    */
  def changePassword(email: String, pwd: String): Future[Boolean] = {
    db.run(userProfileQuery.filter(_.email.toLowerCase === email.toLowerCase)
        .map(user => (user.pwd))
        .update(pwd)) map (_ > 0)
  }

  /**
    *
    * @return . list of user Information
    */
  def getAllUser(): Future[List[UserInfo]] = {
    val queryResult = userProfileQuery.filter(_.isAdmin === false).map(userDetail => {
      userDetail
    }).to[List].result
    db.run(queryResult)
  }

  /**
    *
    * @param id . of user to make that user enable
    * @return . true or false on successful enabling or not
    */
  def enable(id: Int): Future[Boolean] = {
    val valueOfEnable = true
    db.run(userProfileQuery.filter(_.id === id)
        .map(user => (user.isEnable))
        .update(valueOfEnable)) map (_ > 0)
  }

  /**
    *
    * @param id . of user to make that user disable
    * @return . true or false on successful disabling or not
    */
  def disable(id: Int): Future[Boolean] = {
    val valueOfDisable = false
    db.run(userProfileQuery.filter(_.id === id)
        .map(user => (user.isEnable))
        .update(valueOfDisable)) map (_ > 0)
  }
}


trait UserProfileRepositoryTable extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._


  val userProfileQuery: TableQuery[UserInfoProfile] = TableQuery[UserInfoProfile]

  //A Tag marks a specific row represented by an AbstractTable instance.
//scalastyle:off
  private[models] class UserInfoProfile(tag: Tag) extends Table[UserInfo](tag, "userInfo") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def first_name: Rep[String] = column[String]("first_name")

    def middle_name: Rep[String] = column[String]("middle_name")

    def last_name: Rep[String] = column[String]("last_name")

    def email: Rep[String] = column[String]("email")

    def pwd: Rep[String] = column[String]("pwd")

    def mobile_number: Rep[String] = column[String]("mobile_number")

    def gender: Rep[String] = column[String]("gender")

    def age: Rep[Int] = column[Int]("age")

    def hobbies: Rep[String] = column[String]("hobbies")

    def isAdmin: Rep[Boolean] = column[Boolean]("isAdmin")

    def isEnable: Rep[Boolean] = column[Boolean]("isEnable")

    def * : ProvenShape[UserInfo] = (id, first_name, middle_name, last_name, email, pwd, mobile_number, gender, age, hobbies, isAdmin, isEnable) <> (UserInfo.tupled, UserInfo.unapply)
  }
  //scalastyle:on

}