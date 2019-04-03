package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class GridSpec extends WordSpec with Matchers {

  "A Grid" when {
    "to be constructed" should {
      "be created properly but empty" should {
        val grid = Grid()
        grid.init(12, 12, 12)
        val smallGrid = grid
        grid.init(16, 16, 16)
        val normalGrid = grid

        "give access to its cells" in {
          val cell = Cell(false, 0, 'w', None, false)
          smallGrid.matrix(0)(0).getAll() should be(cell.getAll())
          normalGrid.matrix(0)(0).getAll() should be(cell.getAll())
          normalGrid.matrix(0)(1).getAll() should be(cell.getAll())
          normalGrid.matrix(1)(0).getAll() should be(cell.getAll())
          normalGrid.matrix(1)(1).getAll() should be(cell.getAll())
          grid.colIndex(2) should be(1)
          grid.rowIndex(2) should be(-1)
        }

        "allow to set individual cells and remain immutable" in {
          normalGrid.matrix(0)(0).checked = true
          val cell = Cell(true, normalGrid.matrix(0)(1).value, 'w', None, false)
          normalGrid.matrix(0)(0).getAll() should be(cell.getAll())
        }
      }

      "be pre-filled with values 1 to n" should {
        val grid = Grid()
        grid.init(2, 2, 2)
        val tinyGrid = grid

        "have the right values in the right places" in {
          tinyGrid.matrix(0)(0).checked = true
          val cell = Cell(true, 0, 'w', None, false)
          tinyGrid.matrix(0)(0).getAll() should be(cell.getAll())
          val cell2 = Cell(false, 0, 'w', None, false)
          tinyGrid.matrix(0)(1).getAll() should be(cell2.getAll())
          tinyGrid.matrix(1)(0).getAll() should be(cell2.getAll())
          tinyGrid.matrix(1)(1).getAll() should be(cell2.getAll())
          tinyGrid.matrix(0)(1).checked = true
          tinyGrid.matrix(1)(0).checked = true
          tinyGrid.matrix(1)(1).checked = true
        }
      }
    }
  }

}
