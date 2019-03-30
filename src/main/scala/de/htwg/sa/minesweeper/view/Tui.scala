package de.htwg.sa.minesweeper.view

import de.htwg.sa.minesweeper.controller.{CellChanged, ControllerInterface, GridSizeChanged, Winner}

import scala.swing.Reactor
import scala.io.StdIn.readLine

class Tui(controller: ControllerInterface) extends Reactor {

  listenTo(controller)
  println("Type h for help")

  var lastGame: Int = 1
  var status: Int = 0
  var noMineNumber: Int = 0
  var savedHeight: Int = 10
  var savedWidth: Int = 10
  var savedMines: Int = 10

  def processInputLine(input: String): Unit = {
    input match {
      case "h" => {
        println("Type new to start a new game")
        println("Type exit to quit the game")
        println("Type 1 for beginner grid")
        println("Type 2 for advanced grid")
        println("Type 3 for expert grid")
        println("Type 4 to set a custom grid with parameters in the next line (height) (width) (mines)")
        println("Type (row) (column) to set a cell")
        println("Type f (row) (column) to set a flag")
        println("Type s to solve the current game")
        println("Type save to save the current grid")
        println("Type load to load the last saved grid")
      }
      case "1" => {
        createGrid(10, 10, 10)
        status = 0
        noMineNumber = 90
        lastGame = 1
      }
      case "2" => {
        createGrid(16, 16, 40)
        status = 0
        noMineNumber = 186
        lastGame = 2
      }
      case "3" => {
        createGrid(20, 20, 80)
        status = 0
        noMineNumber = 250
        lastGame = 3
      }
      case "4" => {
        val input: String = readLine()
        val inputCustom = input.split(' ').map(c => c.toInt)

        if (inputCustom.length != 3) {
          println("Help: Custom parameters are (height) (width) (mines)")
          return
        } else {
          if (inputCustom(2) >= inputCustom(0) * inputCustom(1)) {
            println("Number of Mines must be smaller than grid size")
            return
          } else if (inputCustom(0) < 10 || inputCustom(1) < 10 || inputCustom(2) < 10) {
            println("Height, Width and Number of Mines must be minimum 10")
            return
          } else if (inputCustom(0) > 35 || inputCustom(1) > 35) {
            println("Height and Width may not exceed 35")
            return
          }

          createGrid(inputCustom(0), inputCustom(1), inputCustom(2))
          noMineNumber = (inputCustom(0) * inputCustom(1)) - inputCustom(2)
          savedHeight = inputCustom(0)
          savedWidth = inputCustom(1)
          savedMines = inputCustom(2)
        }

        status = 0
        lastGame = 4
      }
      case "exit" => {
        System.exit(0)
      }
      case "new" => {
        lastGame match {
          case 1 => {
            createGrid(10, 10, 10)
            status = 0
            noMineNumber = 90
          }
          case 2 => {
            createGrid(16, 16, 40)
            status = 0
            noMineNumber = 186
          }
          case 3 => {
            createGrid(20, 20, 80)
            status = 0
            noMineNumber = 250
          }
          case 4 => {
            createGrid(savedHeight, savedWidth, savedMines)
            status = 0
            noMineNumber = (savedHeight * savedWidth) - savedMines
          }
          case _ => {
            createGrid(10, 10, 10)
            status = 0
            noMineNumber = 90
          }
        }

        controller.createGrid(10, 10, 10)
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

              if (controller.grid.matrix(row - 1)(col - 1).flag) {
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

  def createGrid(height: Int, width: Int, numMines: Int): Unit = {
    controller.createGrid(height, width, numMines)
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
