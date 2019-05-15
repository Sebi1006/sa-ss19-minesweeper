package de.htwg.sa.minesweeper.model.gridcomponent

import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.{Cell, Matrix}

import java.awt.Color

/**
  * A grid interface to define the grid of a minesweeper board.
  */
trait GridInterface {

  val size: Int
  val matrix: Matrix[Cell]

  /**
    * Initialization of the grid with a pre-given number of mines.
    * Never places a mine on the cell of the first click.
    * Does not run until the first click was done.
    *
    * @param rowUsed row of the cell which was first clicked on.
    * @param colUsed column of the cell which was first clicked on.
    * @return the updated Grid.
    */
  def placeMines(rowUsed: Int, colUsed: Int): GridInterface

  /**
    * Calculates the values of each cell of the grid, depending on the number of neighbouring mines.
    * Cells with mines have a value of -1.
    *
    * @return the updated Grid.
    */
  def calculateValues(): GridInterface

  /**
    * Sets the cell status of a specified cell.
    *
    * @param row     row of the cell.
    * @param col     column of the cell.
    * @param checked new value for boolean checked.
    * @return the updated Grid.
    */
  def setChecked(row: Int, col: Int, checked: Boolean): GridInterface

  /**
    * Sets the flag status of a specified cell.
    *
    * @param row  row of the cell.
    * @param col  column of the cell.
    * @param flag new value for boolean flag.
    * @return the updated Grid.
    */
  def setFlag(row: Int, col: Int, flag: Boolean): GridInterface

  /**
    * Sets the color status of a specified cell.
    *
    * @param row   row of the cell.
    * @param col   column of the cell.
    * @param color new value for integer color.
    * @return the updated Grid.
    */
  def setColor(row: Int, col: Int, color: Int): GridInterface

  /**
    * Sets the actual color of a specified cell.
    *
    * @param row       row of the cell.
    * @param col       column of the cell.
    * @param colorBack new value for color.
    * @return the updated Grid.
    */
  def setColorBack(row: Int, col: Int, colorBack: Color): GridInterface

  /**
    * Returns the given index of the array (-1, -1, -1, 0, 1, 1, 1, 0).
    * Is used to make it easier to check for every neighbour.
    *
    * @param i index of the array.
    * @return the value behind the index.
    */
  def rowIndex(i: Int): Int

  /**
    * Returns the given index of the array (-1, 0, 1, 1, 1, 0, -1, -1).
    * Is used to make it easier to check for every neighbour.
    *
    * @param i index of the array.
    * @return the value behind the index.
    */
  def colIndex(i: Int): Int

  /**
    * Solves the grid.
    *
    * @return a list of row and col indices for every cell that was checked that way.
    */
  def solve(): (List[(Int, Int)], GridInterface)

}

/**
  * A cell interface to define the cells of a minesweeper board.
  */
trait CellInterface {

  val checked: Boolean
  val value: Int
  val color: Int
  val colorBack: Option[Color]
  val flag: Boolean

  /**
    * Returns every value of the cell as a tuple.
    *
    * @return checked, value, color, background color, flag.
    */
  def getAll(): (Boolean, Int, Int, Option[Color], Boolean)

}
