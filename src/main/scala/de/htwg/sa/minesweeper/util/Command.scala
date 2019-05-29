package de.htwg.sa.minesweeper.util

trait Command {

  def doStep(): Unit

  def undoStep(): Unit

  def redoStep(): Unit

}
