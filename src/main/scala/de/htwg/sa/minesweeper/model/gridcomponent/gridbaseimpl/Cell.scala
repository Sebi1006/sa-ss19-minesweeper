package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.CellInterface

import java.awt.Color

case class Cell(var checked: Boolean, var value: Int, var color: Int, var colorBack: Option[Color], var flag: Boolean) extends CellInterface {

  def getAll(): (Boolean, Int, Int, Option[Color], Boolean) = {
    (this.checked, this.value, this.color, this.colorBack, this.flag)
  }

}
