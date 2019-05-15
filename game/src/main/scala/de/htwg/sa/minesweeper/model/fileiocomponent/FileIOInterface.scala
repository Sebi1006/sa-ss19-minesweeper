package de.htwg.sa.minesweeper.model.fileiocomponent

import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface

import play.api.libs.json.JsValue

/**
  * An IO interface which defines save and load instructions of minesweeper grids for file IO.
  */
trait FileIOInterface {

  /**
    * Description of the load method which loads a certain file as a minesweeper grid.
    * If successful, the parameters can be used to create a minesweeper grid.
    *
    * @return the grid and the number of mines.
    */
  def load(): (Option[GridInterface], Int)

  /**
    * Description of the save method to save a certain minesweeper grid.
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
