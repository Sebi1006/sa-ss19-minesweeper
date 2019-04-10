package de.htwg.sa.minesweeper.model.fileiocomponent.fileiojsonimpl

import de.htwg.sa.minesweeper.model.fileiocomponent.FileIOInterface
import de.htwg.sa.minesweeper.model.gridcomponent.{CellInterface, GridInterface}
import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Grid

import play.api.libs.json._
import scala.io.Source
import scala.util.Try

class FileIO extends FileIOInterface {

  override def load(): (Option[GridInterface], Int) = {
    var gridOption: Option[GridInterface] = None
    var numMines = 10

    Try {
      val source: String = Source.fromFile("grid.json").getLines.mkString
      val json: JsValue = Json.parse(source)
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
    import java.io._
    val pw = new PrintWriter(new File("grid.json"))
    pw.write(Json.prettyPrint(gridToJson(grid)))
    pw.close()
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

}
