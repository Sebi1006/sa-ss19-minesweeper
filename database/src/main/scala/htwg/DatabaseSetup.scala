package hwtg

import slick.jdbc.H2Profile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json._

class DatabaseSetup {

  var currId = 1
  val db = Database.forConfig("h2mem1")

  val saves: TableQuery[Saves] = TableQuery[Saves]

  var test: String = Json.obj(
    "grid" -> Json.obj(
      "height" -> JsNumber(10),
      "width" -> JsNumber(10),
      "numMines" -> JsNumber(10),
      "cells" -> Json.toJson(
        for {row <- 0 until 10; col <- 0 until 10} yield {
          Json.obj(
            "row" -> row,
            "col" -> col,
            "cell" -> Json.toJson("cell"))
        }
      )
    )
  ).toString()

  val setupAction: DBIO[Unit] = DBIO.seq(
    // Create the schema by combining the DDLs for the Suppliers and Coffees
    // tables using the query interfaces
    saves.schema.create,

    // Insert some suppliers
    saves += (currId, "david", test)
  )

  val setupFuture = db.run(setupAction)

  def load(Id: Int): String = {
    var returnValue = ""
    val a = db.run(saves.result).map(_.foreach {
      case (saveId, userName, saveJson) => {
        if (saveId == Id) {returnValue = saveJson}
      }
    })
    while(!a.isCompleted){}
    returnValue
  }

  def save(gridJson: String): Unit = {
    currId = currId + 1
    val insertValue: DBIO[Unit] = DBIO.seq(
      saves += (currId, "david", gridJson)
    )
    db.run(insertValue)
  }
}