package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface

import java.awt.Color

class Solver(grid: GridInterface) {

  var intList: List[(Int, Int)] = Nil

  def solve(): List[(Int, Int)] = {
    for (i <- 0 until grid.height; j <- 0 until grid.width) {
      if (!grid.matrix(i)(j).checked) {
        intList = (i, j) :: intList
      }

      grid.matrix(i)(j).checked = true
      grid.matrix(i)(j).color = 'b'
      grid.matrix(i)(j).colorBack = Some(Color.LIGHT_GRAY)
    }

    intList
  }

}
