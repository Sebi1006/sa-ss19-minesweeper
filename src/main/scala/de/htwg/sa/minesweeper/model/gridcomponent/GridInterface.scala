package de.htwg.sa.minesweeper.model.gridcomponent

import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Cell

import java.awt.Color

/**
  * A grid interface to define the grid of a minesweeper board.
  */
trait GridInterface {

  var height: Int
  var width: Int
  var numMines: Int
  val matrix: Vector[Vector[Cell]]

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

  var checked: Boolean
  var value: Int
  var color: Int
  var colorBack: Option[Color]
  var flag: Boolean

  /**
    * Returns every value of the cell as a tuple.
    *
    * @return value, checked, color, background color, flag.
    */
  def getAll(): (Int, Boolean, Int, Option[Color], Boolean)

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
