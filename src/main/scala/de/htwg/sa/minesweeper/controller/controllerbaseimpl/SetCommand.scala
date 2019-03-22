package de.htwg.sa.minesweeper.controller.controllerbaseimpl

import de.htwg.sa.minesweeper.util.Command

class SetCommand(row: Int, col: Int, undo: Boolean, group: List[(Int, Int)], typ: Int, controller: Controller) extends Command {

  override def doStep(): Unit = {}

  override def undoStep(): Unit = {
    if (typ == 1) {
      controller.setChecked(row, col, true, false, false)
    } else if (typ == 2) {
      controller.setFlag(row, col, !undo, false)
    } else {
      for (i <- group.indices) {
        controller.setChecked(group(i)._1, group(i)._2, true, false, true)
      }
    }
  }

  override def redoStep(): Unit = {
    if (typ == 1) {
      controller.setChecked(row, col, false, false, false)
    } else if (typ == 2) {
      controller.setFlag(row, col, undo, false)
    } else if (typ == 3) {
      for (i <- group.indices) {
        controller.setChecked(group(i)._1, group(i)._2, false, false, true)
      }
    } else {
      return
    }
  }

}
