package services

import javax.inject.Inject

import models.{AssignmentInfo, AssignmentInterface}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

class DbServiceAssignment @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                    assignmentInterface: AssignmentInterface) {
  /**
    *
    * @param assignmentInfo . assignment that you want to store in db
    * @return . future of true on successful storing of assignment else future of false
    */
  def store(assignmentInfo: AssignmentInfo): Future[Boolean] = {
    assignmentInterface.storeAssignment(assignmentInfo)
  }

  /**
    *
    * @param title . of assignment you want to have detail
    * @return . option assignment information
    */
  def getAssignment(title: String): Future[Option[AssignmentInfo]] = {
    assignmentInterface.getAssignment(title)
  }

  /**
    *
    * @return . list of all assignment
    */
  def getAllAssignment(): Future[List[AssignmentInfo]] = {
    assignmentInterface.getAllAssignment()
  }
  /**
    *
    * @param id . of assignment you want to delete
    * @return . integer of number of rows effected
    */
  def deleteAssignment(id: Int): Future[Int] = {
    assignmentInterface.deleteAssignment(id)
  }

}
