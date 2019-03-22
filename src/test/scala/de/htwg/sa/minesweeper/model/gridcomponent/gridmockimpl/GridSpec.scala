package de.htwg.sa.minesweeper.model.gridcomponent.gridmockimpl

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class GridSpec extends WordSpec with Matchers {

  "A Grid" when {
    "to be constructed" should {
      "do everything correctly" in {
        val grid = new Grid()
        grid.init(10, 10, 10)
        grid.cell(0, 0)
        grid.setMines(0, 0)
        grid.setValues()
        grid.getRow(0)
        grid.getCol(0)
        grid.getHeight()
        grid.getWidth()
        grid.getNumMines()
        grid.solve()

        val emptyCell = EmptyCell
        emptyCell.init(false, 0, 0, null, false)
        emptyCell.getAll()
        emptyCell.setValue(0)
        emptyCell.getValue()
        emptyCell.setChecked(false)
        emptyCell.getChecked()
        emptyCell.setColor(0)
        emptyCell.getColor()
        emptyCell.setColorBack(null)
        emptyCell.getColorBack()
        emptyCell.setFlag(false)
        emptyCell.getFlag()
      }
    }
  }

}
