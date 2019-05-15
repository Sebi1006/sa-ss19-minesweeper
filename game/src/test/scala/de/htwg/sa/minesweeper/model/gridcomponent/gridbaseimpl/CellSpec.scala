package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import java.awt.Color

@RunWith(classOf[JUnitRunner])
class CellSpec extends WordSpec with Matchers {

  "A Cell" when {
    "not set to any value " should {
      val emptyCell = Cell(false, 0, 'w', Some(new Color(255, 255, 255)), false)

      "have value 0" in {
        emptyCell.value should be(0)
      }

      "not be set" in {
        emptyCell.checked should be(false)
      }
    }

    "set to a specific value" should {
      val nonEmptyCell = Cell(true, 5, 'w', Some(new Color(255, 255, 255)), false)

      "return that value" in {
        nonEmptyCell.value should be(5)
      }

      "be set" in {
        nonEmptyCell.checked should be(true)
      }
    }
  }

}
