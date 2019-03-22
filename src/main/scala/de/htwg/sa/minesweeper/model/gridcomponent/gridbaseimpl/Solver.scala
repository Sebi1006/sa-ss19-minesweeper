package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface

import java.awt.Color

class Solver(grid: GridInterface) {

  var intList: List[(Int, Int)] = Nil

  def solve(): List[(Int, Int)] = {
    for (i <- 0 until grid.getHeight(); j <- 0 until grid.getWidth()) {
      if (!grid.cell(i, j).getChecked()) {
        intList = (i, j) :: intList
      }

      grid.cell(i, j).setChecked(true)
      grid.cell(i, j).setColor('b')
      grid.cell(i, j).setColorBack(Color.LIGHT_GRAY)
    }

    intList
  }

}
