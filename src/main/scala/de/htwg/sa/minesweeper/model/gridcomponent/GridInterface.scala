package de.htwg.sa.minesweeper.model.gridcomponent

import java.awt.Color

/**
  * A grid interface to define the grid of a minesweeper board.
  */
trait GridInterface {

  /**
    * Initialization of the grid with different values than the default parameters.
    *
    * @param height   height of the grid.
    * @param width    width of the grid.
    * @param numMines number of mines to place in the grid.
    */
  def init(height: Int, width: Int, numMines: Int): Unit

  /**
    * Returns a specified cell of the given grid.
    *
    * @param row row of the cell.
    * @param col column of the cell.
    * @return the cell specified by the given parameters.
    */
  def cell(row: Int, col: Int): CellInterface

  /**
    * Initialization of the grid with a pre-given number of mines.
    * Never places a mine on the cell of the first click.
    * Does not run until the first click was done.
    *
    * @param rowUsed row of the cell which was first clicked on.
    * @param colUsed column of the cell which was first clicked on.
    */
  def setMines(rowUsed: Int, colUsed: Int): Unit

  /**
    * Calculates the values of each cell of the grid, depending on the number of neighbouring mines.
    * Cells with mines have a value of -1.
    */
  def setValues(): Unit

  /**
    * Returns the given index of the array (-1, -1, -1, 0, 1, 1, 1, 0).
    * Is used to make it easier to check for every neighbour.
    *
    * @param i index of the array.
    * @return the value behind the index.
    */
  def getRow(i: Int): Int

  /**
    * Returns the given index of the array (-1, 0, 1, 1, 1, 0, -1, -1).
    * Is used to make it easier to check for every neighbour.
    *
    * @param i index of the array.
    * @return the value behind the index.
    */
  def getCol(i: Int): Int

  /**
    * Returns the height of the grid.
    *
    * @return the height.
    */
  def getHeight(): Int

  /**
    * Returns the width of the grid.
    *
    * @return the width.
    */
  def getWidth(): Int

  /**
    * Returns the number of mines in the grid.
    *
    * @return the number of mines.
    */
  def getNumMines(): Int

  /**
    * Solves the grid.
    *
    * @return a list of row and col indices for every cell that was checked that way.
    */
  def solve(): List[(Int, Int)]

}

/**
  * An interface which creates a grid with default values.
  */
trait GridFactory {
  /**
    * Creates a grid with default values.
    */
  def create(): GridInterface
}

/**
  * A cell interface to define the cells of a minesweeper board.
  */
trait CellInterface {

  /**
    * Initialization of the cell with different values than the default parameters.
    *
    * @param checked   specifies if the cell is checked.
    * @param value     value of the cell.
    * @param color     color of the cell.
    * @param colorBack a background color for the JButtons of the GUI.
    * @param flag      specifies if the cell is flagged, cannot be flagged when checked.
    */
  def init(checked: Boolean, value: Int, color: Int, colorBack: Color, flag: Boolean): Unit

  /**
    * Returns every value of the cell as a tuple.
    *
    * @return value, checked, color, background color, flag.
    */
  def getAll(): (Int, Boolean, Int, Color, Boolean)

  /**
    * Sets the value of the cell.
    *
    * @param value value to set.
    */
  def setValue(value: Int): Unit

  /**
    * Returns the value of the cell.
    *
    * @return the value.
    */
  def getValue(): Int

  /**
    * Sets the checked status of the cell.
    *
    * @param checked status to set.
    */
  def setChecked(checked: Boolean): Unit

  /**
    * Returns the checked status of the cell.
    *
    * @return the checked status.
    */
  def getChecked(): Boolean

  /**
    * Sets the color of the cell.
    *
    * @param color color to set.
    */
  def setColor(color: Int): Unit

  /**
    * Returns the color of the cell.
    *
    * @return the color.
    */
  def getColor(): Int

  /**
    * Sets the background color of the cell.
    *
    * @param color color to set.
    */
  def setColorBack(color: Color): Unit

  /**
    * Returns the background color of the cell.
    *
    * @return the background color.
    */
  def getColorBack(): Color

  /**
    * Sets the flag status of the cell.
    *
    * @param undo specifies if it should unset it instead.
    */
  def setFlag(undo: Boolean): Unit

  /**
    * Returns the flag status of the cell.
    *
    * @return the flag status.
    */
  def getFlag(): Boolean

}

/**
  * An interface which creates a cell with default parameters.
  */
trait CellFactory {
  /**
    * Creates a cell with default parameters.
    *
    * @return the created cell.
    */
  def create(): CellInterface
}
