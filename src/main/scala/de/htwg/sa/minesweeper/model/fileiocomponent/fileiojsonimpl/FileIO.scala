package de.htwg.sa.minesweeper.model.fileiocomponent.fileiojsonimpl

import de.htwg.sa.minesweeper.MineSweeperModule
import de.htwg.sa.minesweeper.model.fileiocomponent.FileIOInterface
import de.htwg.sa.minesweeper.model.gridcomponent.{CellInterface, GridInterface}

import com.google.inject.Guice
import play.api.libs.json._
import scala.io.Source

class FileIO extends FileIOInterface {

  override def load(): (Int, Int, Int, List[Int], List[Boolean], List[Boolean], List[Int]) = {
    val source: String = Source.fromFile("grid.json").getLines.mkString
    val json: JsValue = Json.parse(source)
    val height = (json \ "grid" \ "height").get.toString.toInt
    val width = (json \ "grid" \ "width").get.toString.toInt
    val numMines = (json \ "grid" \ "numMines").get.toString.toInt
    val injector = Guice.createInjector(new MineSweeperModule())
    var listValue: List[Int] = Nil
    var listChecked: List[Boolean] = Nil
    var listFlag: List[Boolean] = Nil
    var listColor: List[Int] = Nil

    for (i <- 0 until height; j <- 0 until width) {
      val row = (json \\ "row") (i).as[Int]
      val col = (json \\ "col") (j).as[Int]
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

    (height, width, numMines, listValue, listChecked, listFlag, listColor)
  }

  override def save(grid: GridInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("grid.json"))
    pw.write(Json.prettyPrint(gridToJson(grid)))
    pw.close()
  }

  implicit val cellWrites: Writes[CellInterface] = new Writes[CellInterface] {
    def writes(cell: CellInterface): JsObject = Json.obj(
      "value" -> cell.getValue(),
      "checked" -> cell.getChecked(),
      "flag" -> cell.getFlag(),
      "color" -> cell.getColor()
    )
  }

  def gridToJson(grid: GridInterface): JsObject = {
    Json.obj(
      "grid" -> Json.obj(
        "height" -> JsNumber(grid.getHeight()),
        "width" -> JsNumber(grid.getWidth()),
        "numMines" -> JsNumber(grid.getNumMines()),
        "cells" -> Json.toJson(
          for {row <- 0 until grid.getHeight(); col <- 0 until grid.getWidth()} yield {
            Json.obj(
              "row" -> row,
              "col" -> col,
              "cell" -> Json.toJson(grid.cell(row, col)))
          }
        )
      )
    )
  }

}
