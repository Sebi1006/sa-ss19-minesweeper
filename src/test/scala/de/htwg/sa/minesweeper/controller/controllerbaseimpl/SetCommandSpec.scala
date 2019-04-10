package de.htwg.sa.minesweeper.controller.controllerbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Grid

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import scala.language.reflectiveCalls

@RunWith(classOf[JUnitRunner])
class SetCommandSpec extends WordSpec with Matchers {

  "A Controller" when {
    "empty" should {
      val grid = new Grid(10)
      val controller = new Controller(grid)

      "undo a step correctly" in {
        var command = new SetCommand(0, 0, false, Nil, 1, controller)
        command.undoStep()

        command = new SetCommand(0, 0, false, Nil, 2, controller)
        command.undoStep()
      }

      "redo a step correctly" in {
        var command = new SetCommand(0, 0, false, Nil, 1, controller)
        command.redoStep()

        command = new SetCommand(0, 0, false, Nil, 2, controller)
        command.redoStep()
      }
    }
  }

}
