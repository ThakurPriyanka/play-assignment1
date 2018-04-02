package models

import forms.AssignmentInfoForm
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.concurrent.duration.Duration
import scala.concurrent.Await

class AssignmentInterfaceTest extends Specification with Mockito {

  "store the assignment data" in {
    val assignmentInterface = new ModelsTest[AssignmentInterface]
    val assignment = AssignmentInfo(1,"assignment1", "make an app of play")
    val actual = Await.result(assignmentInterface.respository.storeAssignment(assignment), Duration.Inf)
    actual must equalTo(true)
  }


  "find the assignment by title" in {
    val assignmentInterface = new ModelsTest[AssignmentInterface]
    val title = "assignment1"
    val assignment = Option(AssignmentInfo(1,"assignment1", "make an app of play"))
    val actual = Await.result(assignmentInterface.respository.getAssignment(title), Duration.Inf)
    actual must equalTo(assignment)
  }

  "get all assignment" in {
    val assignmentInterface = new ModelsTest[AssignmentInterface]
    val assignment1 = AssignmentInfo(1,"assignment1", "make an app of play")
    val assignmentList = List(assignment1)
    val actual = Await.result(assignmentInterface.respository.getAllAssignment(), Duration.Inf)
    actual must equalTo(assignmentList)
  }

/*  "delete the assignment" in {
    val assignmentInterface = new ModelsTest[AssignmentInterface]
    val id = 1
    val actual = Await.result(assignmentInterface.respository.deleteAssignment(id), Duration.Inf)
    actual must equalTo(1)
  }*/
}
