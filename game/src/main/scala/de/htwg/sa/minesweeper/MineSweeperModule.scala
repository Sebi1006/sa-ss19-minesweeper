package de.htwg.sa.minesweeper

import de.htwg.sa.minesweeper.controller.ControllerInterface
import de.htwg.sa.minesweeper.controller.controllerbaseimpl.Controller
import de.htwg.sa.minesweeper.model.saveandloadcomponent.SaveAndLoadInterface
import de.htwg.sa.minesweeper.model.saveandloadcomponent.saveandloadjsonimpl.SaveAndLoad
import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface
import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.InjectedGrid

import net.codingwell.scalaguice.ScalaModule
import com.google.inject.AbstractModule
import com.google.inject.name.Names

class MineSweeperModule extends AbstractModule with ScalaModule {

  val default: Int = 10

  def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("Default")).to(default)

    bind[GridInterface].to[InjectedGrid]
    bind[ControllerInterface].to[Controller]

    bind[GridInterface].annotatedWithName("beginner").toInstance(new InjectedGrid(10))
    bind[GridInterface].annotatedWithName("intermediate").toInstance(new InjectedGrid(16))
    bind[GridInterface].annotatedWithName("expert").toInstance(new InjectedGrid(20))

    bind[SaveAndLoadInterface].to[SaveAndLoad]
  }

}
