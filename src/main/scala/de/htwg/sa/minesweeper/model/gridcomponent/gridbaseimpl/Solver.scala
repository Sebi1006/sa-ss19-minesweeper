package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface

import java.awt.Color

class Solver(grid: GridInterface) {

  def solve(): (List[(Int, Int)], GridInterface) = {
    var intList: List[(Int, Int)] = Nil
    var gridBase = grid

    for (i <- 0 until grid.size; j <- 0 until grid.size) {
      if (!grid.matrix.cell(i, j).checked) {
        intList = (i, j) :: intList
      }

      gridBase = gridBase.setChecked(i, j, true)
      gridBase = gridBase.setColor(i, j, 'b')
      gridBase = gridBase.setColorBack(i, j, Color.LIGHT_GRAY)
    }

    (intList, gridBase)
  }

}
