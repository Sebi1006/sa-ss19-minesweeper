package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import com.google.inject.Inject
import com.google.inject.name.Named

class InjectedGrid @Inject()(@Named("Default") gridSize: Int) extends Grid(gridSize) {
  new Grid(gridSize)
}
