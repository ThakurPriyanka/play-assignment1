package models

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import services.DbServiceAssignment

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class AssignmentInterfaceTest extends Specification with Mockito {

  "store the assignment data" in {
    val assignmentInterface = mock[AssignmentInterface]
    val assignment = AssignmentInfo(0,"assignment1", "make an app of play")
    assignmentInterface.storeAssignment(assignment) returns Future.successful(true)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val assignmentService = new DbServiceAssignment(db, assignmentInterface)
    val actual = Await.result(assignmentService.store(assignment), Duration.Inf)
    actual must equalTo(true)
  }

  "find the assignment by title" in {
    val assignmentInterface = mock[AssignmentInterface]
    val title = "assignment1"
    val assignment = Option(AssignmentInfo(0,"assignment1", "make an app of play"))
    assignmentInterface.getAssignment(title) returns Future.successful(assignment)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val assignmentService = new DbServiceAssignment(db, assignmentInterface)
    val actual = Await.result(assignmentService.getAssignment(title), Duration.Inf)
    actual must equalTo(assignment)
  }

  "get all assignment" in {
    val assignmentInterface = mock[AssignmentInterface]
    val assignment1 = AssignmentInfo(0,"assignment1", "make an app of play")
    val assignment2 = AssignmentInfo(0,"assignment1", "make an app of play")
    val assignmentList = List(assignment1, assignment2)
    assignmentInterface.getAllAssignment() returns Future.successful(assignmentList)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val assignmentService = new DbServiceAssignment(db, assignmentInterface)
    val actual = Await.result(assignmentService.getAllAssignment(), Duration.Inf)
    actual must equalTo(assignmentList)
  }

  "delete the assignment" in {
    val assignmentInterface = mock[AssignmentInterface]
    val id = 1
    assignmentInterface.deleteAssignment(id) returns Future.successful(1)
    val db = mock[play.api.db.slick.DatabaseConfigProvider]
    val assignmentService = new DbServiceAssignment(db, assignmentInterface)
    val actual = Await.result(assignmentService.deleteAssignment(id), Duration.Inf)
    actual must equalTo(1)
  }
}
