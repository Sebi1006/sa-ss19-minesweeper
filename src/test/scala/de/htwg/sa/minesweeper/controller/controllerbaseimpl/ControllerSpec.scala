package de.htwg.sa.minesweeper.controller.controllerbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Grid

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import scala.language.reflectiveCalls

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {

  "A Controller" when {
    "empty" should {
      val grid = Grid()
      val controller = new Controller(grid)

      "handle undo/redo of solving a grid correctly" in {
        controller.getAll(1, 1) should be((false, false, 0, 'w', 10, 10, null, false))
        controller.grid.cell(0, 0).getChecked() should be(false)
        controller.solve()
        controller.grid.cell(0, 0).getChecked() should be(true)
        controller.undo()
        controller.grid.cell(0, 0).getChecked() should be(false)
        controller.redo()
        controller.grid.cell(0, 0).getChecked() should be(false)
        controller.grid.cell(0, 0).setFlag(false)
        controller.undo()
        controller.grid.cell(0, 0).getFlag() should be(false)
        controller.redo()
        controller.grid.cell(0, 0).getFlag() should be(false)
        controller.grid.cell(1, 0).setChecked(true)
        controller.undo()
        controller.grid.cell(1, 0).getChecked() should be(false)
        controller.redo()
        controller.grid.cell(1, 0).getChecked() should be(false)

        var row: Int = 0
        var col: Int = 0

        for (i <- 0 until 10; j <- 0 until 10) {
          if (controller.grid.cell(i, j).getValue() == 0) {
            controller.setChecked(i, j, false, false, false)
            row = i
            col = j
          }
        }

        controller.undo()
        controller.getChecked(row, col) should be(false)
        controller.redo()
        controller.getChecked(row, col) should be(true)
      }

      "create a grid" in {
        controller.createGrid(15, 15, 15)
        controller.grid.getHeight() should be(15)
        controller.grid.getWidth() should be(15)
        controller.grid.getNumMines() should be(15)
      }
    }

    "without mines" should {
      val grid = Grid()
      grid.init(10, 10, 0)
      val controller = new Controller(grid)
      controller.setChecked(0, 0, false, false, false)

      "solve with depthFirstSearch everything" in {
        controller.depthFirstSearch(0, 0)
        controller.grid.cell(9, 9).getChecked() should be(true)
      }

      "after save and load" in {
        controller.save()
        controller.createGrid(10, 10, 10)
        controller.load()
        controller.grid.cell(9, 9).getChecked() should be(true)
      }
    }

    "only mines" should {
      val grid = Grid()
      grid.init(2, 2, 2)
      val controller = new Controller(grid)
      controller.setChecked(0, 0, false, false, false)

      "solve" in {
        for (i <- 0 until 2) {
          if (controller.getValue(1, i) == -1) {
            controller.getMine(1, i) should be(true)
            controller.setChecked(1, i, false, false, false)
            controller.getChecked(1, i) should be(true)
            controller.winner(1, i, true)
            controller.getStatus() should be(0)
          }
        }
      }
    }

    "big grid" should {
      val grid = Grid()
      grid.init(30, 30, 30)
      val controller = new Controller(grid)
      controller.setChecked(0, 0, false, false, false)

      "dpfs should get" in {
        for (i <- 0 until 30; j <- 0 until 30) {
          if (controller.getValue(i, j) == 0) {
            controller.setChecked(i, j, false, false, false)
            controller.getChecked(i, j) should be(true)
          }
        }
      }
    }
  }

}
