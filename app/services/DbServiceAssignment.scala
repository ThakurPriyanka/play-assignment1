package services

import javax.inject.Inject

import models.{AssignmentInfo, AssignmentInterface}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

class DbServiceAssignment @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                    assignmentInterface: AssignmentInterface) {

  def store(assignmentInfo: AssignmentInfo): Future[Boolean] = {
    assignmentInterface.storeAssignment(assignmentInfo)
  }


  def getAssignment(title: String): Future[Option[AssignmentInfo]] = {
    assignmentInterface.getAssignment(title)
  }


  def getAllAssignment(): Future[List[AssignmentInfo]] = {
    assignmentInterface.getAllAssignment()
  }

  def deleteAssignment(id: Int): Future[Int] = {
    assignmentInterface.deleteAssignment(id)
  }

}
