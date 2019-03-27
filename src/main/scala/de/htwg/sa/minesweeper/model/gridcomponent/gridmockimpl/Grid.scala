package de.htwg.sa.minesweeper.model.gridcomponent.gridmockimpl

import java.awt.Color

import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Cell
import de.htwg.sa.minesweeper.model.gridcomponent.{CellInterface, GridInterface}

class Grid extends GridInterface {

  var height: Int = 0
  var width: Int = 0
  var numMines: Int = 0
  val matrix: Vector[Vector[Cell]] = Vector.tabulate(height, width) { (_, _) => new Cell(false, 0, 'w', None, false) }

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

  def getAll(): (Int, Boolean, Int, Option[Color], Boolean) = (0, false, 'w', None, false)

}
