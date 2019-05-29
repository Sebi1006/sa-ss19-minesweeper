package de.htwg.sa.minesweeper.model.saveandloadcomponent

import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface

import akka.http.scaladsl.model.HttpResponse
import play.api.libs.json.JsValue
import scala.concurrent.Future

/**
  * An interface which defines save and load instructions of minesweeper grids.
  */
trait SaveAndLoadInterface {

  /**
    * A load method which requests a minesweeper grid.
    *
    * @return the Future containing a HttpResponse.
    */
  def load(): Future[HttpResponse]

  /**
    * A load method which loads a minesweeper grid from JSON.
    * If successful, the parameters can be used to create a minesweeper grid.
    *
    * @param jsValue the JsValue.
    * @return the grid and the number of mines.
    */
  def loadFromJson(jsValue: JsValue): (Option[GridInterface], Int)

  /**
    * The save method to save a certain minesweeper grid.
    *
    * @param grid the grid the user wants to save.
    */
  def save(grid: GridInterface): Unit

  /**
    * Gets the JSON grid.
    *
    * @param grid the grid.
    * @return the JsValue.
    */
  def getJsonGrid(grid: GridInterface): JsValue

}
