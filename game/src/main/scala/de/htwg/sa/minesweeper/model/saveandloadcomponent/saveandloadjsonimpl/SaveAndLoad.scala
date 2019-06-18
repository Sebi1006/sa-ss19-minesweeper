package de.htwg.sa.minesweeper.model.saveandloadcomponent.saveandloadjsonimpl

import de.htwg.sa.minesweeper.model.saveandloadcomponent.SaveAndLoadInterface
import de.htwg.sa.minesweeper.model.gridcomponent.{CellInterface, GridInterface}
import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Grid

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.util.Try
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import play.api.libs.json._

class SaveAndLoad extends SaveAndLoadInterface {

  implicit val actorSystem: ActorSystem = ActorSystem("SaveAndLoadSystem")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  override def load(): Future[HttpResponse] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
      method = HttpMethods.GET,
      uri = "http://localhost:8889/load"))

    responseFuture
  }

  override def loadFromJson(jsValue: JsValue): (Option[GridInterface], Int) = {
    var gridOption: Option[GridInterface] = None
    var numMines = 10

    Try {
      val json: JsValue = jsValue
      val height = (json \ "grid" \ "height").get.toString.toInt
      val width = (json \ "grid" \ "width").get.toString.toInt
      numMines = (json \ "grid" \ "numMines").get.toString.toInt
      var listValue: List[Int] = Nil
      var listChecked: List[Boolean] = Nil
      var listFlag: List[Boolean] = Nil
      var listColor: List[Int] = Nil

      for (i <- 0 until height; j <- 0 until width) {
        val cell = (json \\ "cell") (j + (i * width))
        val value = (cell \ "value").as[Int]
        listValue = value :: listValue
        val checked = (cell \ "checked").as[Boolean]
        listChecked = checked :: listChecked
        val flag = (cell \ "flag").as[Boolean]
        listFlag = flag :: listFlag
        val color = (cell \ "color").as[Int]
        listColor = color :: listColor
      }

      gridOption = Some(Grid.loadGrid(height: Int, width: Int, numMines: Int, listValue: List[Int],
        listChecked: List[Boolean], listFlag: List[Boolean], listColor: List[Int]))
    }

    (gridOption, numMines)
  }

  override def save(grid: GridInterface): Unit = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
      method = HttpMethods.GET,
      uri = "http://localhost:8889/save",
      entity = HttpEntity(ContentTypes.`application/json`, Json.prettyPrint(gridToJson(grid)).toString)
    ))

    responseFuture.onComplete {
      case Success(_) => println("The current minesweeper grid was successfully saved.")
      case Failure(e) => println(e)
    }
  }

  implicit val cellWrites: Writes[CellInterface] = (cell: CellInterface) => Json.obj(
    "value" -> cell.value,
    "checked" -> cell.checked,
    "flag" -> cell.flag,
    "color" -> cell.color
  )

  def gridToJson(grid: GridInterface): JsObject = {
    if (grid.size == 10) {
      Json.obj(
        "grid" -> Json.obj(
          "height" -> JsNumber(grid.size),
          "width" -> JsNumber(grid.size),
          "numMines" -> JsNumber(10),
          "cells" -> Json.toJson(
            for {row <- 0 until grid.size; col <- 0 until grid.size} yield {
              Json.obj(
                "row" -> row,
                "col" -> col,
                "cell" -> Json.toJson(grid.matrix.cell(row, col)))
            }
          )
        )
      )
    } else if (grid.size == 16) {
      Json.obj(
        "grid" -> Json.obj(
          "height" -> JsNumber(grid.size),
          "width" -> JsNumber(grid.size),
          "numMines" -> JsNumber(40),
          "cells" -> Json.toJson(
            for {row <- 0 until grid.size; col <- 0 until grid.size} yield {
              Json.obj(
                "row" -> row,
                "col" -> col,
                "cell" -> Json.toJson(grid.matrix.cell(row, col)))
            }
          )
        )
      )
    } else {
      Json.obj(
        "grid" -> Json.obj(
          "height" -> JsNumber(grid.size),
          "width" -> JsNumber(grid.size),
          "numMines" -> JsNumber(80),
          "cells" -> Json.toJson(
            for {row <- 0 until grid.size; col <- 0 until grid.size} yield {
              Json.obj(
                "row" -> row,
                "col" -> col,
                "cell" -> Json.toJson(grid.matrix.cell(row, col)))
            }
          )
        )
      )
    }
  }

  override def getJsonGrid(grid: GridInterface): JsValue = {
    gridToJson(grid)
  }

}
