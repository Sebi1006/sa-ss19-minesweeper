package de.htwg.sa.minesweeper.model.fileiocomponent

import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface

/**
  * An IO interface which defines save and load instructions of minesweeper grids for file IO.
  */
trait FileIOInterface {

  /**
    * Description of the load method which loads a certain file as a minesweeper grid.
    * If successful, the parameters can be used to create a minesweeper grid.
    *
    * @return height, width, number of mines, list of cell values, list of cell checked states,
    *         list of cell flag states, list of cell colors.
    */
  def load(): (Int, Int, Int, List[Int], List[Boolean], List[Boolean], List[Int])

  /**
    * Description of the save method to save a certain minesweeper grid.
    *
    * @param grid the grid the user wants to save.
    */
  def save(grid: GridInterface): Unit

}
