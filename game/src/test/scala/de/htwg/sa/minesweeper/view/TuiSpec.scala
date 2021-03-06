package de.htwg.sa.minesweeper.view

import de.htwg.sa.minesweeper.controller.controllerbaseimpl.Controller
import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Grid

import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TuiSpec extends WordSpec with Matchers {

  "A Minesweeper TUI" should {
    val grid = new Grid(10)
    val controller = new Controller(grid)
    val tui = new Tui(controller)

    "print help" in {
      tui.processInputLine("h")
    }

    "create an empty minesweeper on input 'new' for the first time" in {
      tui.lastGame = 0
      tui.processInputLine("new")
      val grid = new Grid(10)
      controller.grid should be(grid)
    }

    "create an empty minesweeper on input '1'" in {
      tui.processInputLine("1")
      val grid = new Grid(10)
      controller.grid should be(grid)
    }

    "create an empty minesweeper on input 'new' for the second time" in {
      tui.processInputLine("new")
      val grid = new Grid(10)
      controller.grid should be(grid)
    }

    "create an empty minesweeper on input '2'" in {
      tui.processInputLine("2")
      val grid = new Grid(16)
      controller.grid should be(grid)
    }

    "create an empty minesweeper on input '3'" in {
      tui.processInputLine("3")
      val grid = new Grid(20)
      controller.grid should be(grid)
    }

    "set a cell on input '2 2'" in {
      val input = "2 2"
      tui.processInputLine(input)
      controller.grid.matrix.cell(1, 1).checked should be(true)
    }

    "set a flag on an unchecked cell" in {
      for (i <- 0 until 9; j <- 0 until 9) {
        if (!controller.grid.matrix.cell(i, j).checked) {
          val input = "f " + (i + 1).toString + " " + (j + 1).toString
          tui.processInputLine(input)
          controller.grid.matrix.cell(i, j).flag should be(true)
        }
      }
    }

    "not set a flag on input '9 9 9'" in {
      val input = "9 9 9"
      tui.processInputLine(input)
    }

    "not do anything on input '1 2 3 4 5'" in {
      val input = "1 2 3 4 5"
      tui.processInputLine(input)
    }

    "solve a minesweeper game on input 's'" in {
      tui.processInputLine("s")
      controller.grid.matrix.cell(9, 9).checked should be(true)
    }

    "lose a minesweeper game" in {
      tui.processInputLine("1")

      for (i <- 0 until 10; j <- 0 until 10) {
        val input = (i + 1).toString + " " + (j + 1).toString
        tui.processInputLine(input)
      }
    }

    "win a minesweeper game" in {
      tui.processInputLine("1")
      tui.processInputLine("1 1")

      for (i <- 0 until 10; j <- 0 until 10) {
        if (controller.grid.matrix.cell(i, j).value == -1) {
          val input = "f " + (i + 1).toString + " " + (j + 1).toString
          tui.processInputLine(input)
        } else if (!controller.grid.matrix.cell(i, j).checked) {
          val input = (i + 1).toString + " " + (j + 1).toString
          tui.processInputLine(input)
        }
      }
    }
  }

}
