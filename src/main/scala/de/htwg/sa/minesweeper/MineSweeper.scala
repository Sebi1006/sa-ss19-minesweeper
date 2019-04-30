package de.htwg.sa.minesweeper

import de.htwg.sa.minesweeper.view.{Gui, Tui, RestApi}
import de.htwg.sa.minesweeper.controller.ControllerInterface

import scala.io.StdIn.readLine
import com.google.inject.{Guice, Injector}

object MineSweeper {

  val injector: Injector = Guice.createInjector(new MineSweeperModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  val gui = new Gui(controller)
  val rest = new RestApi(controller)

  def main(args: Array[String]): Unit = {
    var input: String = ""
    rest.startRestApi()

    do {
      input = readLine()
      tui.processInputLine(input)
    } while (input != "exit")
  }

}
