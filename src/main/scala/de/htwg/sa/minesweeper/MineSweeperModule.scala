package de.htwg.sa.minesweeper

import de.htwg.sa.minesweeper.controller.ControllerInterface
import de.htwg.sa.minesweeper.controller.controllerbaseimpl.Controller
import de.htwg.sa.minesweeper.model.fileiocomponent.FileIOInterface
import de.htwg.sa.minesweeper.model.fileiocomponent.fileiojsonimpl.FileIO
import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface
import de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl.Grid

import net.codingwell.scalaguice.ScalaModule
import com.google.inject.AbstractModule

class MineSweeperModule extends AbstractModule with ScalaModule {

  def configure(): Unit = {
    bind[GridInterface].to[Grid]
    bind[ControllerInterface].to[Controller]
    bind[FileIOInterface].to[FileIO]
  }

}
