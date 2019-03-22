package de.htwg.sa.minesweeper

import de.htwg.sa.minesweeper.view.{Gui, Tui}
import de.htwg.sa.minesweeper.controller.{ControllerFactory, ControllerInterface}
import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Grid

import scala.io.StdIn.readLine
import net.codingwell.scalaguice.InjectorExtensions._
import com.google.inject.Guice

object MineSweeper {

  var controller: ControllerInterface = Guice.createInjector(new MineSweeperModule).instance[ControllerFactory].create(Grid())
  val tui = new Tui(controller)
  val gui = new Gui(controller)

  def main(args: Array[String]): Unit = {
    var input: String = ""

    do {
      input = readLine()
      tui.processInputLine(input)
    } while (input != "exit")
  }

}
