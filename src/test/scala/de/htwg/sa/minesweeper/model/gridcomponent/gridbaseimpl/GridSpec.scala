package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class GridSpec extends WordSpec with Matchers {

  "A Grid" when {
    "to be constructed" should {
      "be created with the height and width of the given parameters and a given number of mines" in {
        val grid = Grid(10, 10, 10)
        val tinyGrid = Grid(12, 12, 12)
        val smallGrid = Grid(16, 16, 16)
        val normalGrid = Grid(10, 14, 22)
      }

      "created properly but empty" should {
        val grid = Grid(10, 10, 10)
        val tinyGrid = Grid(12, 12, 12)
        val smallGrid = Grid(16, 16, 16)
        val normalGrid = Grid(10, 14, 22)

        "give access to its cells" in {
          val cell = new Cell(false, 0, 'w', None, false)
          tinyGrid.matrix(0)(0).getAll() should be(cell.getAll())
          smallGrid.matrix(0)(0).getAll() should be(cell.getAll())
          smallGrid.matrix(0)(1).getAll() should be(cell.getAll())
          smallGrid.matrix(1)(0).getAll() should be(cell.getAll())
          smallGrid.matrix(1)(1).getAll() should be(cell.getAll())
          grid.colIndex(2) should be(1)
          grid.rowIndex(2) should be(-1)
        }

        "allow to set individual cells and remain immutable" in {
          smallGrid.matrix(0)(0).checked = true
          val cell = new Cell(true, smallGrid.matrix(0)(1).value, 'w', None, false)
          smallGrid.matrix(0)(0).getAll() should be(cell.getAll())
        }
      }

      "pre-filled with values 1 to n" should {
        val grid = Grid(2, 2, 2)
        val tinyGrid = grid

        "have the right values in the right places" in {
          tinyGrid.matrix(0)(0).checked = true
          val cell = new Cell(true, 0, 'w', None, false)
          tinyGrid.matrix(0)(0).getAll() should be(cell.getAll())
          val cell2 = new Cell(false, 0, 'w', None, false)
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
