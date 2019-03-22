package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.CellInterface

import com.google.inject.assistedinject.AssistedInject
import java.awt.Color

class Cell @AssistedInject()() extends CellInterface {

  var checked = false
  var value = 0
  var color: Int = 'w'
  var colorBack: Color = _
  var flag = false

  def init(checked: Boolean, value: Int, color: Int, colorBack: Color, flag: Boolean): Unit = {
    setChecked(checked)
    setValue(value)
    setColor(color)
    setColorBack(colorBack)
    setFlag(flag)
  }

  def getAll(): (Int, Boolean, Int, Color, Boolean) = {
    (getValue(), getChecked(), getColor(), getColorBack(), getFlag())
  }

  def setValue(value: Int): Unit = {
    this.value = value
  }

  def getValue(): Int = value

  def setChecked(checked: Boolean): Unit = {
    this.checked = checked
  }

  def getChecked(): Boolean = checked

  def setColor(color: Int): Unit = {
    this.color = color
  }

  def getColor(): Int = color

  def setColorBack(color: Color): Unit = {
    this.colorBack = color
  }

  def getColorBack(): Color = colorBack

  def setFlag(flag: Boolean): Unit = {
    this.flag = flag
  }

  def getFlag(): Boolean = flag

}
