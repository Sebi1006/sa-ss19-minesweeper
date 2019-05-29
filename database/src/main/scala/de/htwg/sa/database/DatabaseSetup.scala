package de.htwg.sa.database

import slick.jdbc.H2Profile.api._
import scala.concurrent.ExecutionContext.Implicits.global

class DatabaseSetup {

  val db = Database.forConfig("h2mem1")
  val saves: TableQuery[Saves] = TableQuery[Saves]
  val id = 1
  var counter = 0

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
    if (counter == 0) {
      val insertValue: DBIO[Unit] = DBIO.seq(
        saves.schema.create,
        saves += (id, "master", gridJson)
      )

      db.run(insertValue)
      counter = counter + 1
    } else {
      val updateValue = saves.filter(_.id === 1)
        .map(save => save.save)
        .update(gridJson)

      db.run(updateValue)
    }
  }

}
