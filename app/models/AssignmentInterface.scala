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
      with AssignmentBaseRepositoryImpl with AssignmentRepositoryTable {}




  trait AssignmentBaseRepository {
    def storeAssignment(assignmentInfo: AssignmentInfo): Future[Boolean]

    def getAssignment(title: String): Future[Option[AssignmentInfo]]

    def getAllAssignment(): Future[List[AssignmentInfo]]

    def deleteAssignment(id: Int): Future[Int]
  }


  trait AssignmentBaseRepositoryImpl extends AssignmentBaseRepository {
    self: AssignmentRepositoryTable =>

    import profile.api._

    def storeAssignment(assignemt: AssignmentInfo): Future[Boolean] =
      db.run(assignemtQuery += assignemt) map (_ > 0)

    def getAssignment(title: String): Future[Option[AssignmentInfo]] = {
      val queryResult = assignemtQuery.filter(_.title.toLowerCase === title.toLowerCase).result.headOption
      db.run(queryResult)
    }

    def getAllAssignment(): Future[List[AssignmentInfo]] = {
      val queryResult = assignemtQuery.map(assignmentDetail  => {
        assignmentDetail
      } ).to[List].result
      db.run(queryResult)
    }
    def deleteAssignment(id: Int): Future[Int] = {
      db.run(assignemtQuery.filter(_.id === id).delete)
    }
  }




  trait AssignmentRepositoryTable extends HasDatabaseConfigProvider[JdbcProfile] {

    import profile.api._


    val assignemtQuery: TableQuery[AssignmentDetail] = TableQuery[AssignmentDetail]
    //A Tag marks a specific row represented by an AbstractTable instance.

    private[models] class AssignmentDetail(tag: Tag) extends Table[AssignmentInfo](tag, "assignmentDetail") {
      def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

      def title: Rep[String] = column[String]("title")

      def description: Rep[String] = column[String]("description")

      def * : ProvenShape[AssignmentInfo] = (id, title, description) <>(AssignmentInfo.tupled, AssignmentInfo.unapply)
    }


  }
