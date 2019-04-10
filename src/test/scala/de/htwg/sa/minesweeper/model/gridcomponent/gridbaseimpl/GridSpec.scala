package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class GridSpec extends WordSpec with Matchers {

  "A Grid" when {
    "to be constructed" should {
      "be created properly but empty" should {
        val beginnerGrid = new Grid(10)
        val intermediateGrid = new Grid(16)

        "give access to its cells" in {
          val cell = Cell(false, 0, 'w', None, false)
          beginnerGrid.matrix.cell(0, 0).getAll() should be(cell.getAll())
          intermediateGrid.matrix.cell(0, 0).getAll() should be(cell.getAll())
          intermediateGrid.matrix.cell(0, 1).getAll() should be(cell.getAll())
          intermediateGrid.matrix.cell(1, 0).getAll() should be(cell.getAll())
          intermediateGrid.matrix.cell(1, 1).getAll() should be(cell.getAll())
          intermediateGrid.colIndex(2) should be(1)
          intermediateGrid.rowIndex(2) should be(-1)
        }

        "allow to set individual cells and remain immutable" in {
          val intermediateGrid2 = intermediateGrid.setChecked(0, 0, true)
          val cell = Cell(true, intermediateGrid2.matrix.cell(0, 1).value, 'w', None, false)
          intermediateGrid2.matrix.cell(0, 0).getAll() should be(cell.getAll())
        }
      }

      "be pre-filled with values 1 to n" should {
        val normalGrid = new Grid(10)

        "have the right values in the right places" in {
          val normalGrid2 = normalGrid.setChecked(0, 0, true)
          val cell = Cell(true, 0, 'w', None, false)
          normalGrid2.matrix.cell(0, 0).getAll() should be(cell.getAll())
          val cell2 = Cell(false, 0, 'w', None, false)
          normalGrid2.matrix.cell(0, 1).getAll() should be(cell2.getAll())
          normalGrid2.matrix.cell(1, 0).getAll() should be(cell2.getAll())
          normalGrid2.matrix.cell(1, 1).getAll() should be(cell2.getAll())
          normalGrid2.setChecked(0, 1, true)
          normalGrid2.setChecked(1, 0, true)
          normalGrid2.setChecked(1, 1, true)
        }
      }
    }
  }

}
