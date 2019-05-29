package de.htwg.sa.minesweeper

import de.htwg.sa.minesweeper.server.{DatabaseSetup, ServerController}

object Server {

  def main(args: Array[String]): Unit = {
    val database = new DatabaseSetup()
    val server = new ServerController(database)
    server.startServer()
  }

}
