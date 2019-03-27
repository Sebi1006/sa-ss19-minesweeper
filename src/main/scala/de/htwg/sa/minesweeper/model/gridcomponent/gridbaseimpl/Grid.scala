package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface

import com.google.inject.Inject
import scala.util.Random

case class Grid @Inject()(var height: Int, var width: Int, var numMines: Int) extends GridInterface {

  val InitialValue = 10

  val row: Array[Int] = Array(-1, -1, -1, 0, 1, 1, 1, 0)
  val col: Array[Int] = Array(-1, 0, 1, 1, 1, 0, -1, -1)
  val matrix: Vector[Vector[Cell]] = Vector.tabulate(height, width) { (_, _) => new Cell(false, 0, 'w', None, false) }

  height = InitialValue
  width = InitialValue
  numMines = InitialValue

  def setMines(rowUsed: Int, colUsed: Int): Unit = {
    val rand: Random = new Random()
    var row: Int = 0
    var col: Int = 0
    var i: Int = 0

    while (i < numMines) {
      row = rand.nextInt(height)
      col = rand.nextInt(width)

      if (matrix(row)(col).value != -1 && !(row == rowUsed && col == colUsed)) {
        matrix(row)(col).value = -1
        matrix(row)(col).color = 'b'
        i += 1
      }
    }
  }

  def setValues(): Unit = {
    for (i <- 0 until height; j <- 0 until width) {
      var value: Int = 0
      var rowC: Int = 0
      var colC: Int = 0

      if (matrix(i)(j).value != -1) {
        for (k <- 0 until 8) {
          rowC = i + row(k)
          colC = j + col(k)

          if (rowC >= 0 && colC >= 0 && rowC < height && colC < width) {
            if (matrix(rowC)(colC).value == -1) {
              value += 1
              value - 1
            }
          }
        }

        matrix(i)(j).value = value
      }
    }
  }

  def rowIndex(i: Int): Int = {
    this.row(i)
  }

  def colIndex(i: Int): Int = {
    this.col(i)
  }

  def solve(): List[(Int, Int)] = {
    val s = new Solver(this)
    s.solve()
  }

  override def toString: String = {
    val lineSeparator = ("+-" + ("--" * width)) + "+\n"
    val line = ("| " + ("y " * width)) + "|\n"
    var box = "\n" + (lineSeparator + (line * height)) + lineSeparator

    for {
      row <- 0 until height
      col <- 0 until width
    } if (!matrix(row)(col).checked) {
      if (!matrix(row)(col).flag) {
        box = box.replaceFirst("y", "x")
      } else {
        box = box.replaceFirst("y", "f")
      }
    } else {
      if (matrix(row)(col).value == -1) {
        box = box.replaceFirst("y", "b")
      } else {
        box = box.replaceFirst("y", matrix(row)(col).value.toString)
      }
    }

    box
  }

}
