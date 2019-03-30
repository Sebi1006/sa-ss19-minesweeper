package de.htwg.sa.minesweeper.controller.controllerbaseimpl

import de.htwg.sa.minesweeper.controller.{CellChanged, ControllerInterface, GridSizeChanged, Winner}
import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface
import de.htwg.sa.minesweeper.util.UndoManager
import de.htwg.sa.minesweeper.MineSweeperModule
import de.htwg.sa.minesweeper.model.fileiocomponent.FileIOInterface

import com.google.inject.Inject
import net.codingwell.scalaguice.InjectorExtensions._
import com.google.inject.{Guice, Injector}
import scala.swing.Publisher
import java.awt.Color

class Controller @Inject()(var grid: GridInterface) extends ControllerInterface with Publisher {

  publish(GridSizeChanged(grid.height, grid.width, grid.numMines))

  private var undoManager = new UndoManager()

  val injector: Injector = Guice.createInjector(new MineSweeperModule())
  val fileIo: FileIOInterface = injector.instance[FileIOInterface]

  var noMineCount: Int = (grid.height * grid.width) - grid.numMines
  var mineFound: Int = 0
  var flag: Boolean = true
  var intList: List[(Int, Int)] = Nil
  var status = 0

  def createGrid(height: Int, width: Int, numMines: Int): Unit = {
    grid = injector.instance[GridInterface]
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
    grid = injector.instance[GridInterface]
    grid.init(height, width, numMines)

    for (i <- 0 until height; j <- 0 until width) {
      grid.matrix(i)(j).value = values(width - j - 1 + (height - 1 - i) * width)
      grid.matrix(i)(j).checked = checked(width - j - 1 + (height - 1 - i) * width)
      grid.matrix(i)(j).flag = flagList(width - j - 1 + (height - 1 - i) * width)
      grid.matrix(i)(j).color = color(width - j - 1 + (height - 1 - i) * width)

      if (grid.matrix(i)(j).value != 0) {
        flag = false
      }
    }

    for (i <- 0 until height; j <- 0 until width) {
      if (grid.matrix(i)(j).checked) {
        grid.matrix(i)(j).colorBack = Some(Color.LIGHT_GRAY)
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
        if (grid.matrix(row)(col).checked) {
          return
        }

        if (grid.matrix(row)(col).flag) {
          mineFound += 1
        }

        grid.matrix(row)(col).checked = true

        if (flag) {
          grid.setMines(row, col)
          grid.setValues()
          flag = false
        }

        if (grid.matrix(row)(col).value == 0) {
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
        if (!grid.matrix(row)(col).checked) {
          return
        }

        grid.matrix(row)(col).checked = false
        winner(row, col, undo)
      }

      if (!dpfs) {
        publish(CellChanged())
      }
    }
  }

  def getMine(row: Int, col: Int): Boolean = {
    if (grid.matrix(row)(col).value == -1) {
      return true
    }

    false
  }

  def setFlag(row: Int, col: Int, undo: Boolean, command: Boolean): Unit = {
    if (!grid.matrix(row)(col).checked) {
      if (command) {
        undoManager.doStep(new SetCommand(row, col, undo, intList, 2, this))
      }

      grid.matrix(row)(col).flag = !undo

      if (undo) {
        mineFound += 1
      } else {
        mineFound -= 1
      }

      publish(CellChanged())
    }
  }

  def depthFirstSearch(rowD: Int, colD: Int): Unit = {
    var row: Int = 0
    var col: Int = 0

    if (!grid.matrix(rowD)(colD).checked) {
      intList = (rowD, colD) :: intList
    }

    grid.matrix(rowD)(colD).color = 'b'
    grid.matrix(rowD)(colD).colorBack = Some(Color.LIGHT_GRAY)
    setChecked(rowD, colD, false, false, true)

    for (i <- 0 until 8) {
      row = rowD + grid.rowIndex(i)
      col = colD + grid.colIndex(i)

      if (row >= 0 && row < grid.height && col >= 0 && col < grid.width && grid.matrix(row)(col).color == 'w') {
        if (grid.matrix(row)(col).value == 0 && !grid.matrix(row)(col).checked) {
          depthFirstSearch(row, col)
        } else {
          if (!grid.matrix(row)(col).checked) {
            intList = (row, col) :: intList
          }

          setChecked(row, col, false, false, true)
          grid.matrix(row)(col).color = 'b'
        }
      }
    }
  }

  def winner(row: Int, col: Int, undo: Boolean): Unit = {
    if (!undo) {
      if (grid.matrix(row)(col).value != -1) {
        status = 0
        noMineCount -= 1
      } else {
        for (i <- 0 until grid.height; j <- 0 until grid.width) {
          if (grid.matrix(row)(col).value == -1) {
            grid.matrix(row)(col).checked = true
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
      if (grid.matrix(row)(col).value == -1) {
        status = 0

        for (i <- 0 until grid.height; j <- 0 until grid.width) {
          if (grid.matrix(row)(col).value == -1) {
            grid.matrix(row)(col).checked = false
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

  def getAll(row: Int, col: Int): (Boolean, Boolean, Int, Int, Int, Int, Option[Color], Boolean) = {
    (grid.matrix(row)(col).checked, getMine(row, col), grid.matrix(row)(col).value, grid.matrix(row)(col).color,
      grid.height, grid.width, grid.matrix(row)(col).colorBack, grid.matrix(row)(col).flag)
  }

}
