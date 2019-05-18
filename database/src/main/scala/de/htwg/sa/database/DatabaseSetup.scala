package de.htwg.sa.database

import slick.jdbc.H2Profile.api._
import scala.concurrent.ExecutionContext.Implicits.global

class DatabaseSetup {

  val db = Database.forConfig("h2mem1")
  val saves: TableQuery[Saves] = TableQuery[Saves]
  val id = 1

  def load(): String = {
    var loadedJson = ""

    val getJsonValue = db.run(saves.result).map(_.foreach {
      case (saveId, _, saveJson) => {
        if (saveId == id) {
          loadedJson = saveJson
        }
      }
    })

    while (!getJsonValue.isCompleted) {}

    loadedJson
  }

  def save(gridJson: String): Unit = {
    val insertValue: DBIO[Unit] = DBIO.seq(
      saves.schema.create,
      saves += (id, "master", gridJson)
    )

    db.run(insertValue)
  }

}
