package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface
import de.htwg.sa.minesweeper.controller.controllerbaseimpl.MyActor.{SolverMessage, ControllerMessage}

import akka.actor.{Actor, ActorRef}
import java.awt.Color

class Solver(grid: GridInterface, controller: ActorRef) extends Actor {

  def solve(value: (List[(Int, Int)], GridInterface)): (List[(Int, Int)], GridInterface) = {
    var intList: List[(Int, Int)] = Nil
    var gridBase = value._2

    for (i <- 0 until gridBase.size; j <- 0 until gridBase.size) {
      if (!gridBase.matrix.cell(i, j).checked) {
        intList = (i, j) :: intList
      }

      gridBase = gridBase.setChecked(i, j, true)
      gridBase = gridBase.setColor(i, j, 'b')
      gridBase = gridBase.setColorBack(i, j, Color.LIGHT_GRAY)
    }

    (intList, gridBase)
  }

  override def receive: Receive = {
    case ControllerMessage(value) => controller ! SolverMessage(solve(value))
  }

}
