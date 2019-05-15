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
      val grid = new Grid(10)
      val controller = new Controller(grid)

      "handle undo/redo of solving a grid correctly" in {
        controller.getAll(1, 1) should be((false, false, 0, 'w', 10, 10, None, false))
        controller.grid.matrix.cell(0, 0).checked should be(false)
        controller.solve()
        controller.grid.matrix.cell(0, 0).checked should be(true)
        controller.undo()
        controller.grid.matrix.cell(0, 0).checked should be(false)
        controller.redo()
        controller.grid.matrix.cell(0, 0).checked should be(false)
        controller.grid.setFlag(0, 0, false)
        controller.undo()
        controller.grid.matrix.cell(0, 0).flag should be(false)
        controller.redo()
        controller.grid.matrix.cell(0, 0).flag should be(false)
        controller.grid.setChecked(0, 0, true)
        controller.undo()
        controller.grid.matrix.cell(0, 0).checked should be(false)
        controller.redo()
        controller.grid.matrix.cell(0, 0).checked should be(false)

        var row: Int = 0
        var col: Int = 0

        for (i <- 0 until 10; j <- 0 until 10) {
          if (controller.grid.matrix.cell(i, j).value == 0) {
            controller.setChecked(i, j, false, false, false)
            row = i
            col = j
          }
        }

        controller.undo()
        controller.grid.matrix.cell(row, col).checked should be(false)
        controller.redo()
        controller.grid.matrix.cell(row, col).checked should be(true)
      }

      "create a grid" in {
        controller.createGrid(10)
        controller.grid.size should be(10)
      }
    }

    "big grid" should {
      val grid = new Grid(20)
      val controller = new Controller(grid)
      controller.setChecked(0, 0, false, false, false)

      "dpfs work correctly" in {
        for (i <- 0 until 20; j <- 0 until 20) {
          if (controller.grid.matrix.cell(i, j).value == 0) {
            controller.setChecked(i, j, false, false, false)
            controller.grid.matrix.cell(i, j).checked should be(true)
          }
        }
      }
    }
  }

}
