package de.htwg.sa.minesweeper.view

import de.htwg.sa.minesweeper.controller.{CellChanged, ControllerInterface, GridSizeChanged, Winner}

import scala.swing.Reactor

class Tui(controller: ControllerInterface) extends Reactor {

  listenTo(controller)
  println("Type h for help")

  var lastGame: Int = 1
  var status: Int = 0
  var noMineNumber: Int = 0

  def processInputLine(input: String): Unit = {
    input match {
      case "h" => {
        println("Type new to start a new game")
        println("Type exit to quit the game")
        println("Type 1 for beginner grid")
        println("Type 2 for advanced grid")
        println("Type 3 for expert grid")
        println("Type (row) (column) to set a cell")
        println("Type f (row) (column) to set a flag")
        println("Type s to solve the current game")
        println("Type save to save the current grid")
        println("Type load to load the last saved grid")
      }
      case "1" => {
        createGrid(10)
        status = 0
        noMineNumber = 90
        lastGame = 1
      }
      case "2" => {
        createGrid(16)
        status = 0
        noMineNumber = 216
        lastGame = 2
      }
      case "3" => {
        createGrid(20)
        status = 0
        noMineNumber = 320
        lastGame = 3
      }
      case "exit" => {
        System.exit(0)
      }
      case "new" => {
        lastGame match {
          case 1 => {
            createGrid(10)
            status = 0
            noMineNumber = 90
          }
          case 2 => {
            createGrid(16)
            status = 0
            noMineNumber = 216
          }
          case 3 => {
            createGrid(20)
            status = 0
            noMineNumber = 320
          }
          case _ => {
            createGrid(10)
            status = 0
            noMineNumber = 90
          }
        }

        controller.createGrid(10)
      }
      case "s" => {
        controller.solve()
      }
      case "save" => {
        controller.save()
      }
      case "load" => {
        controller.load()
      }
      case _ => {
        if (status == 0) {
          val vec = input.split(' ')

          if (vec.length != 2 && vec.length != 3) {
            println("Wrong Number of Arguments")
            return
          } else if (vec.length == 2) {
            val row = vec(0).toString.toInt
            val col = vec(1).toString.toInt
            controller.setChecked(row - 1, col - 1, false, true, false)
          } else {
            if (vec(0).toString.equals("f")) {
              val row = vec(1).toString.toInt
              val col = vec(2).toString.toInt

              if (controller.grid.matrix.cell(row - 1, col - 1).flag) {
                controller.setFlag(row - 1, col - 1, true, true)
              } else {
                controller.setFlag(row - 1, col - 1, false, true)
              }
            } else {
              println("Please use f to set a flag")
            }
          }
        }
      }
    }
  }

  def createGrid(size: Int): Unit = {
    controller.createGrid(size)
  }

  reactions += {
    case event: GridSizeChanged => {
      println(controller.grid.toString)
      status = 0
    }
    case event: CellChanged => println(controller.grid.toString)
    case event: Winner => {
      if (event.win) {
        println("Hurray! You win!")
        status = 1
      } else {
        println("Game Over!")
        status = 2
      }
    }
  }

}
