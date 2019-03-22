package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class GridSpec extends WordSpec with Matchers {

  "A Grid" when {
    "to be constructed" should {
      "be created with the height and width of the given parameters and a given number of mines" in {
        val grid = Grid()
        val tinyGrid = grid
        grid.init(12, 12, 12)
        val smallGrid = grid
        grid.init(16, 16, 16)
        val normalGrid = grid
        grid.init(10, 14, 22)
      }

      "created properly but empty" should {
        val grid = Grid()
        val tinyGrid = grid
        grid.init(12, 12, 12)
        val smallGrid = grid
        grid.init(16, 16, 16)
        val normalGrid = grid
        grid.init(10, 14, 22)

        "give access to its cells" in {
          val cell = new Cell()
          cell.init(false, 0, 'w', null, false)
          tinyGrid.cell(0, 0).getAll() should be(cell.getAll())
          smallGrid.cell(0, 0).getAll() should be(cell.getAll())
          smallGrid.cell(0, 1).getAll() should be(cell.getAll())
          smallGrid.cell(1, 0).getAll() should be(cell.getAll())
          smallGrid.cell(1, 1).getAll() should be(cell.getAll())
          grid.getCol(2) should be(1)
          grid.getRow(2) should be(-1)
        }

        "allow to set individual cells and remain immutable" in {
          smallGrid.cell(0, 0).setChecked(true)
          val cell = new Cell()
          cell.init(true, smallGrid.cell(0, 1).getValue(), 'w', null, false)
          smallGrid.cell(0, 0).getAll() should be(cell.getAll())
        }
      }

      "pre-filled with values 1 to n" should {
        val grid = Grid()
        grid.init(2, 2, 2)
        val tinyGrid = grid

        "have the right values in the right places" in {
          tinyGrid.cell(0, 0).setChecked(true)
          val cell = new Cell()
          cell.init(true, 0, 'w', null, false)
          tinyGrid.cell(0, 0).getAll() should be(cell.getAll())
          cell.init(false, 0, 'w', null, false)
          tinyGrid.cell(0, 1).getAll() should be(cell.getAll())
          tinyGrid.cell(1, 0).getAll() should be(cell.getAll())
          tinyGrid.cell(1, 1).getAll() should be(cell.getAll())
          tinyGrid.cell(0, 1).setChecked(true)
          tinyGrid.cell(1, 0).setChecked(true)
          tinyGrid.cell(1, 1).setChecked(true)
        }
      }
    }
  }

}
