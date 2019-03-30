package de.htwg.sa.minesweeper.model.gridcomponent.gridmockimpl

import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Cell
import de.htwg.sa.minesweeper.model.gridcomponent.{CellInterface, GridInterface}

import java.awt.Color

class Grid extends GridInterface {

  var height: Int = 0
  var width: Int = 0
  var numMines: Int = 0
  var matrix: Vector[Vector[Cell]] = Vector.tabulate(height, width) { (_, _) => Cell(false, 0, 'w', None, false) }

  def init(height: Int, width: Int, numMines: Int): Unit = {}

  def setMines(rowUsed: Int, colUsed: Int): Unit = {}

  def setValues(): Unit = {}

  def rowIndex(i: Int): Int = 0

  def colIndex(i: Int): Int = 0

  def solve(): List[(Int, Int)] = Nil

}

object EmptyCell extends CellInterface {

  var checked: Boolean = false
  var value: Int = 0
  var color: Int = 0
  var colorBack: Option[Color] = None
  var flag: Boolean = false

  def getAll(): (Boolean, Int, Int, Option[Color], Boolean) = (false, 0, 'w', None, false)

}
