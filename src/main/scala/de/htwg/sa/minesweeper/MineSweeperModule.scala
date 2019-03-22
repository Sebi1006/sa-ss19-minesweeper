package de.htwg.sa.minesweeper

import de.htwg.sa.minesweeper.controller.{ControllerFactory, ControllerInterface}
import de.htwg.sa.minesweeper.controller.controllerbaseimpl.Controller
import de.htwg.sa.minesweeper.model.fileiocomponent.FileIOInterface
import de.htwg.sa.minesweeper.model.fileiocomponent.fileiojsonimpl.FileIO
import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Grid
import de.htwg.sa.minesweeper.model.gridcomponent.{CellFactory, CellInterface, GridFactory, GridInterface}
import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Cell

import net.codingwell.scalaguice.ScalaModule
import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder

class MineSweeperModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    install(new FactoryModuleBuilder().implement(classOf[GridInterface], classOf[Grid]).build(classOf[GridFactory]))
    install(new FactoryModuleBuilder().implement(classOf[CellInterface], classOf[Cell]).build(classOf[CellFactory]))
    install(new FactoryModuleBuilder().implement(classOf[ControllerInterface], classOf[Controller]).build(classOf[ControllerFactory]))
    bind[CellInterface].to[Cell]
    bind[FileIOInterface].to[FileIO]
  }

}
