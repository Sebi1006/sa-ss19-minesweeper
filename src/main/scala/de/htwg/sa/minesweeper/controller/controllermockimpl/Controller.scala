package de.htwg.sa.minesweeper.controller.controllermockimpl

import de.htwg.sa.minesweeper.controller.ControllerInterface
import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface
import de.htwg.sa.minesweeper.model.gridcomponent.gridmockimpl.Grid

class Controller(var grid: GridInterface) extends ControllerInterface {

  grid = new Grid()

  var noMineCount: Int = 0
  var mineFound: Int = 0
  var flag: Boolean = false

  def createGrid(height: Int, width: Int, numMines: Int): Unit = {}

  def setChecked(row: Int, col: Int, undo: Boolean, command: Boolean, dpfs: Boolean): Unit = {}

  def getMine(row: Int, col: Int): Boolean = false

  def setFlag(row: Int, col: Int, undo: Boolean, command: Boolean): Unit = {}

  def depthFirstSearch(rowD: Int, colD: Int): Unit = {}

  def winner(row: Int, col: Int, undo: Boolean): Unit = {}

  def undo(): Unit = {}

  def redo(): Unit = {}

  def solve(): Unit = {}

  def save(): Unit = {}

  def load(): Unit = {}

}
