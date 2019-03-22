package de.htwg.sa.minesweeper.controller.controllermockimpl

import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Grid

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import scala.language.reflectiveCalls

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {

  "A mock controller" when {
    "empty" should {
      val grid = Grid()
      val controller = new Controller(grid)

      "do everything correctly" in {
        controller.createGrid(10, 10, 10)
        controller.setChecked(0, 0, false, false, false)
        controller.getChecked(0, 0)
        controller.getMine(0, 0)
        controller.getValue(0, 0)
        controller.setColor(0, 0, 0)
        controller.getColor(0, 0)
        controller.height()
        controller.width()
        controller.setColorBack(0, 0, null)
        controller.getColorBack(0, 0)
        controller.setFlag(0, 0, false, false)
        controller.getFlag(0, 0)
        controller.depthFirstSearch(0, 0)
        controller.winner(0, 0, false)
        controller.undo()
        controller.redo()
        controller.solve()
        controller.save()
        controller.load()
      }
    }
  }

}
