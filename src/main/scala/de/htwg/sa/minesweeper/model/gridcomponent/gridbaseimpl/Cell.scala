package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.CellInterface

import com.google.inject.Inject
import java.awt.Color

class Cell @Inject()(var checked: Boolean, var value: Int, var color: Int, var colorBack: Option[Color], var flag: Boolean) extends CellInterface {

  def getAll(): (Int, Boolean, Int, Option[Color], Boolean) = {
    (this.value, this.checked, this.color, this.colorBack, this.flag)
  }

}
