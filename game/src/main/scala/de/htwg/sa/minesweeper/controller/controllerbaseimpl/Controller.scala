package de.htwg.sa.minesweeper.controller.controllerbaseimpl

import de.htwg.sa.minesweeper.controller.{CellChanged, ControllerInterface, GridSizeChanged, Winner}
import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface
import de.htwg.sa.minesweeper.util.UndoManager
import de.htwg.sa.minesweeper.MineSweeperModule
import de.htwg.sa.minesweeper.model.saveandloadcomponent.SaveAndLoadInterface

import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.google.inject.name.Names
import com.google.inject.{Guice, Inject, Injector}
import scala.util.{Failure, Success}
import scala.swing.Publisher
import scala.concurrent.{ExecutionContextExecutor, Future}
import net.codingwell.scalaguice.InjectorExtensions._
import play.api.libs.json.{JsValue, Json}
import java.awt.Color

class Controller @Inject()(var grid: GridInterface) extends ControllerInterface with Publisher {

  implicit val actorSystem: ActorSystem = ActorSystem("ControllerSystem")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  private val undoManager = new UndoManager()
  val injector: Injector = Guice.createInjector(new MineSweeperModule())
  val saveAndLoad: SaveAndLoadInterface = injector.instance[SaveAndLoadInterface]

  var noMineCount: Int = 0
  var mineFound: Int = 0
  var minesSet: Boolean = true
  var intList: List[(Int, Int)] = Nil
  var status = 0

  def createGrid(size: Int): Unit = {
    size match {
      case 10 => {
        grid = injector.instance[GridInterface](Names.named("beginner"))
        publish(GridSizeChanged(10, 10, 10))
        noMineCount = 90
        mineFound = 10
      }
      case 16 => {
        grid = injector.instance[GridInterface](Names.named("intermediate"))
        publish(GridSizeChanged(16, 16, 40))
        noMineCount = 216
        mineFound = 40
      }
      case 20 => {
        grid = injector.instance[GridInterface](Names.named("expert"))
        publish(GridSizeChanged(20, 20, 80))
        noMineCount = 320
        mineFound = 80
      }
    }

    status = 0
    minesSet = true
    intList = Nil
  }

  def setChecked(row: Int, col: Int, undo: Boolean, command: Boolean, dfs: Boolean): Unit = {
    if (status == 0 || command) {
      if (command) {
        undoManager.doStep(new SetCommand(row, col, undo, intList, 1, this))
      }

      if (!undo) {
        if (grid.matrix.cell(row, col).checked) {
          return
        }

        if (grid.matrix.cell(row, col).flag) {
          mineFound += 1
        }

        grid = grid.setChecked(row, col, true)

        if (minesSet) {
          grid = grid.placeMines(row, col)
          grid = grid.calculateValues()
          minesSet = false
        }

        if (grid.matrix.cell(row, col).value == 0) {
          if (!dfs) {
            intList = (row, col) :: intList
          }

          depthFirstSearch(row, col)

          if (!dfs) {
            undoManager.doStep(new SetCommand(0, 0, true, intList, 3, this))
          }

          publish(CellChanged())
        }

        winner(row, col, undo)
      } else {
        if (!grid.matrix.cell(row, col).checked) {
          return
        }

        grid = grid.setChecked(row, col, false)
        winner(row, col, undo)
      }

      if (!dfs) {
        publish(CellChanged())
      }
    }
  }

  def getMine(row: Int, col: Int): Boolean = {
    if (grid.matrix.cell(row, col).value == -1) {
      return true
    }

    false
  }

  def setFlag(row: Int, col: Int, undo: Boolean, command: Boolean): Unit = {
    if (!grid.matrix.cell(row, col).checked) {
      if (command) {
        undoManager.doStep(new SetCommand(row, col, undo, intList, 2, this))
      }

      grid = grid.setFlag(row, col, !undo)

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

    if (!grid.matrix.cell(rowD, colD).checked) {
      intList = (rowD, colD) :: intList
    }

    grid = grid.setColor(rowD, colD, 'b')
    grid = grid.setColorBack(rowD, colD, Color.LIGHT_GRAY)
    setChecked(rowD, colD, false, false, true)

    for (i <- 0 until 8) {
      row = rowD + grid.rowIndex(i)
      col = colD + grid.colIndex(i)

      if (row >= 0 && row < grid.size && col >= 0 && col < grid.size && grid.matrix.cell(row, col).color == 'w') {
        if (grid.matrix.cell(row, col).value == 0 && !grid.matrix.cell(row, col).checked) {
          depthFirstSearch(row, col)
        } else {
          if (!grid.matrix.cell(row, col).checked) {
            intList = (row, col) :: intList
          }

          setChecked(row, col, false, false, true)
          grid = grid.setColor(row, col, 'b')
        }
      }
    }
  }

  def winner(row: Int, col: Int, undo: Boolean): Unit = {
    if (!undo) {
      if (grid.matrix.cell(row, col).value != -1) {
        status = 0
        noMineCount -= 1
      } else {
        for (_ <- 0 until grid.size; _ <- 0 until grid.size) {
          if (grid.matrix.cell(row, col).value == -1) {
            grid = grid.setChecked(row, col, true)
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
      if (grid.matrix.cell(row, col).value == -1) {
        status = 0

        for (_ <- 0 until grid.size; _ <- 0 until grid.size) {
          if (grid.matrix.cell(row, col).value == -1) {
            grid = grid.setChecked(row, col, false)
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
    val tuple = grid.solve()
    intList = tuple._1
    grid = tuple._2
    undoManager.doStep(new SetCommand(0, 0, true, intList, 4, this))
    publish(CellChanged())
  }

  def save(): Unit = {
    saveAndLoad.save(grid)
    publish(CellChanged())
  }

  def load(): Unit = {
    val gridFromDatabase = saveAndLoad.load()
    gridFromDatabase.onComplete {
      case Success(res) => {
        val responseAsString: Future[String] = Unmarshal(res.entity).to[String]
        responseAsString.onComplete {
          case Success(res) => {
            val json: JsValue = Json.parse(res)
            val loadedGrid = saveAndLoad.loadFromJson(json)
            val gridOption = loadedGrid._1
            val numMines = loadedGrid._2

            gridOption match {
              case Some(newGrid) => grid = newGrid
              case None => println("Error!")
            }

            noMineCount = (grid.size * grid.size) - numMines
            mineFound = numMines
            intList = Nil

            publish(GridSizeChanged(grid.size, grid.size, numMines))
            publish(CellChanged())
          }
          case Failure(e) => println(e)
        }
      }
      case Failure(e) => println(e)
    }
  }

  def getJsonGrid(): JsValue = {
    saveAndLoad.getJsonGrid(grid)
  }

  def getAll(row: Int, col: Int): (Boolean, Boolean, Int, Int, Int, Int, Option[Color], Boolean) = {
    (grid.matrix.cell(row, col).checked, getMine(row, col), grid.matrix.cell(row, col).value, grid.matrix.cell(row, col).color,
      grid.size, grid.size, grid.matrix.cell(row, col).colorBack, grid.matrix.cell(row, col).flag)
  }

}
