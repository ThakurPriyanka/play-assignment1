package models

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


case class AssignmentInfo(id: Int, title: String, description: String)

class AssignmentInterface @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
    extends AssignmentBaseRepository
        with AssignmentBaseRepositoryImpl with AssignmentRepositoryTable



trait AssignmentBaseRepository {
  def storeAssignment(assignmentInfo: AssignmentInfo): Future[Boolean]

  def getAssignment(title: String): Future[Option[AssignmentInfo]]

  def getAllAssignment(): Future[List[AssignmentInfo]]

  def deleteAssignment(id: Int): Future[Int]
}


trait AssignmentBaseRepositoryImpl extends AssignmentBaseRepository {
  self: AssignmentRepositoryTable =>

  import profile.api._

  /**
    *
    * @param assignment . assignment that you want to store in db
    * @return . future of true on successful storing of assignment else future of false
    */
  def storeAssignment(assignment: AssignmentInfo): Future[Boolean] =
    db.run(assignmentQuery += assignment) map (_ > 0)

  /**
    *
    * @param title . of assignment you want to have detail
    * @return . option assignment information
    */
  def getAssignment(title: String): Future[Option[AssignmentInfo]] = {
    val queryResult = assignmentQuery.filter(_.title.toLowerCase === title.toLowerCase).result.headOption
    db.run(queryResult)
  }

  /**
    *
    * @return . list of all assignment
    */
  def getAllAssignment(): Future[List[AssignmentInfo]] = {
    val queryResult = assignmentQuery.map(assignmentDetail => {
      assignmentDetail
    }).to[List].result
    db.run(queryResult)
  }

  /**
    *
    * @param id . of assignment you want to delete
    * @return . integer of number of rows effected
    */
  def deleteAssignment(id: Int): Future[Int] = {
    db.run(assignmentQuery.filter(_.id === id).delete)
  }
}


trait AssignmentRepositoryTable extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._


  val assignmentQuery: TableQuery[AssignmentDetail] = TableQuery[AssignmentDetail]

  //A Tag marks a specific row represented by an AbstractTable instance.

  private[models] class AssignmentDetail(tag: Tag) extends Table[AssignmentInfo](tag, "assignmentDetail") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def title: Rep[String] = column[String]("title")

    def description: Rep[String] = column[String]("description")

    def * : ProvenShape[AssignmentInfo] = (id, title, description) <> (AssignmentInfo.tupled, AssignmentInfo.unapply)
  }


}
