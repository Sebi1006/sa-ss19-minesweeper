package de.htwg.sa.minesweeper.controller.controllerbaseimpl

import de.htwg.sa.minesweeper.controller.{CellChanged, ControllerInterface, GridSizeChanged, Winner}
import de.htwg.sa.minesweeper.model.gridcomponent.{GridFactory, GridInterface}
import de.htwg.sa.minesweeper.util.UndoManager
import de.htwg.sa.minesweeper.MineSweeperModule
import de.htwg.sa.minesweeper.model.fileiocomponent.FileIOInterface

import com.google.inject.assistedinject.{Assisted, AssistedInject}
import net.codingwell.scalaguice.InjectorExtensions._
import com.google.inject.{Guice, Injector}
import scala.swing.Publisher
import java.awt.Color

class Controller @AssistedInject()(@Assisted var grid: GridInterface) extends ControllerInterface with Publisher {

  publish(GridSizeChanged(grid.getHeight(), grid.getWidth(), grid.getNumMines()))

  private var undoManager = new UndoManager()
  val injector: Injector = Guice.createInjector(new MineSweeperModule())
  val fileIo: FileIOInterface = injector.instance[FileIOInterface]
  var noMineCount: Int = (grid.getHeight() * grid.getWidth()) - grid.getNumMines()
  var mineFound: Int = 0
  var flag: Boolean = true
  var intList: List[(Int, Int)] = Nil
  var status = 0

  def createGrid(height: Int, width: Int, numMines: Int): Unit = {
    grid = injector.instance[GridFactory].create()
    grid.init(height, width, numMines)
    status = 0
    noMineCount = (height * width) - numMines
    mineFound = numMines
    flag = true
    undoManager = new UndoManager()
    intList = Nil
    publish(GridSizeChanged(height, width, numMines))
  }

  def createLoadedGrid(height: Int, width: Int, numMines: Int, values: List[Int],
                       checked: List[Boolean], flagList: List[Boolean], color: List[Int]): Unit = {
    grid = injector.instance[GridFactory].create()
    grid.init(height, width, numMines)

    for (i <- 0 until height; j <- 0 until width) {
      grid.cell(i, j).setValue(values(width - j - 1 + (height - 1 - i) * width))
      grid.cell(i, j).setChecked(checked(width - j - 1 + (height - 1 - i) * width))
      grid.cell(i, j).setFlag(flagList(width - j - 1 + (height - 1 - i) * width))
      grid.cell(i, j).setColor(color(width - j - 1 + (height - 1 - i) * width))

      if (grid.cell(i, j).getValue() != 0) {
        flag = false
      }
    }

    for (i <- 0 until height; j <- 0 until width) {
      if (grid.cell(i, j).getChecked()) {
        grid.cell(i, j).setColorBack(Color.LIGHT_GRAY)
      }
    }

    noMineCount = (height * width) - numMines
    mineFound = numMines
    undoManager = new UndoManager()
    intList = Nil
    publish(GridSizeChanged(height, width, numMines))
  }

  def setChecked(row: Int, col: Int, undo: Boolean, command: Boolean, dpfs: Boolean): Unit = {
    if (status == 0 || command) {
      if (command) {
        undoManager.doStep(new SetCommand(row, col, undo, intList, 1, this))
      }

      if (!undo) {
        if (grid.cell(row, col).getChecked()) {
          return
        }

        if (grid.cell(row, col).getFlag()) {
          mineFound += 1
        }

        grid.cell(row, col).setChecked(true)

        if (flag) {
          grid.setMines(row, col)
          grid.setValues()
          flag = false
        }

        if (grid.cell(row, col).getValue() == 0) {
          if (!dpfs) {
            intList = (row, col) :: intList
          }

          depthFirstSearch(row, col)

          if (!dpfs) {
            undoManager.doStep(new SetCommand(0, 0, true, intList, 3, this))
          }

          publish(CellChanged())
        }

        winner(row, col, undo)
      } else {
        if (!grid.cell(row, col).getChecked()) {
          return
        }

        grid.cell(row, col).setChecked(false)
        winner(row, col, undo)
      }

      if (!dpfs) {
        publish(CellChanged())
      }
    }
  }

  def getChecked(row: Int, col: Int): Boolean = {
    grid.cell(row, col).getChecked()
  }

  def getMine(row: Int, col: Int): Boolean = {
    if (grid.cell(row, col).getValue() == -1) {
      return true
    }

    false
  }

  def getValue(row: Int, col: Int): Int = {
    grid.cell(row, col).getValue()
  }

  def setColor(row: Int, col: Int, color: Int): Unit = {
    grid.cell(row, col).setColor(color)
  }

  def getColor(row: Int, col: Int): Int = {
    grid.cell(row, col).getColor()
  }

  def height(): Int = {
    grid.getHeight()
  }

  def width(): Int = {
    grid.getWidth()
  }

  def setColorBack(row: Int, col: Int, color: Color): Unit = {
    grid.cell(row, col).setColorBack(color)
  }

  def getColorBack(row: Int, col: Int): Color = {
    grid.cell(row, col).getColorBack()
  }

  def setFlag(row: Int, col: Int, undo: Boolean, command: Boolean): Unit = {
    if (!grid.cell(row, col).getChecked()) {
      if (command) {
        undoManager.doStep(new SetCommand(row, col, undo, intList, 2, this))
      }

      grid.cell(row, col).setFlag(!undo)

      if (undo) {
        mineFound += 1
      } else {
        mineFound -= 1
      }

      publish(CellChanged())
    }
  }

  def getFlag(row: Int, col: Int): Boolean = {
    grid.cell(row, col).getFlag()
  }

  def depthFirstSearch(rowD: Int, colD: Int): Unit = {
    var R: Int = 0
    var C: Int = 0

    if (!getChecked(rowD, colD)) {
      intList = (rowD, colD) :: intList
    }

    setColor(rowD, colD, 'b')
    setColorBack(rowD, colD, Color.LIGHT_GRAY)
    setChecked(rowD, colD, false, false, true)

    for (i <- 0 until 8) {
      R = rowD + grid.getRow(i)
      C = colD + grid.getCol(i)

      if (R >= 0 && R < height && C >= 0 && C < width && getColor(R, C) == 'w') {
        if (getValue(R, C) == 0 && !getChecked(R, C)) {
          depthFirstSearch(R, C)
        } else {
          if (!getChecked(R, C)) {
            intList = (R, C) :: intList
          }

          setChecked(R, C, false, false, true)
          setColor(R, C, 'b')
        }
      }
    }
  }

  def winner(row: Int, col: Int, undo: Boolean): Unit = {
    if (!undo) {
      if (grid.cell(row, col).getValue() != -1) {
        status = 0
        noMineCount -= 1
      } else {
        for (i <- 0 until grid.getHeight(); j <- 0 until grid.getWidth()) {
          if (grid.cell(i, j).getValue() == -1) {
            grid.cell(i, j).setChecked(true)
          }
        }

        status = 1
        publish(Winner(false))
      }

      if (noMineCount == 0) {
        status = 2
        publish(Winner(true))
      }
    } else {
      if (grid.cell(row, col).getValue() == -1) {
        status = 0

        for (i <- 0 until grid.getHeight(); j <- 0 until grid.getWidth()) {
          if (grid.cell(i, j).getValue() == -1) {
            grid.cell(i, j).setChecked(false)
          }
        }
      } else {
        status = 0
        noMineCount += 1
      }

      if (noMineCount == 0) {
        status = 1
        publish(Winner(true))
      }
    }
  }

  def undo(): Unit = {
    undoManager.undoStep()
    publish(CellChanged())
  }

  def redo(): Unit = {
    undoManager.redoStep()
    publish(CellChanged())
  }

  def solve(): Unit = {
    intList = grid.solve()
    undoManager.doStep(new SetCommand(0, 0, true, intList, 4, this))
    publish(CellChanged())
  }

  def save(): Unit = {
    fileIo.save(grid)
    publish(CellChanged())
  }

  def load(): Unit = {
    val gridInfo = fileIo.load()
    createLoadedGrid(gridInfo._1, gridInfo._2, gridInfo._3, gridInfo._4, gridInfo._5, gridInfo._6, gridInfo._7)
    publish(CellChanged())
  }

  def getAll(row: Int, col: Int): (Boolean, Boolean, Int, Int, Int, Int, Color, Boolean) = {
    (getChecked(row, col), getMine(row, col), getValue(row, col), getColor(row, col),
      height(), width(), getColorBack(row, col), getFlag(row, col))
  }

  def getStatus(): Int = {
    this.status
  }

}
