package de.htwg.sa.minesweeper.model.gridcomponent.gridbaseimpl

import de.htwg.sa.minesweeper.model.gridcomponent.GridInterface

import java.awt.Color
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Grid(matrix: Matrix[Cell], size: Int) extends GridInterface {

  def this(size: Int) = this(new Matrix[Cell](size, Cell(false, 0, 'w', None, false)), size)

  val rowArray: Array[Int] = Array(-1, -1, -1, 0, 1, 1, 1, 0)
  val colArray: Array[Int] = Array(-1, 0, 1, 1, 1, 0, -1, -1)

  def setCell(matrix: Matrix[Cell], row: Int, col: Int, cell: Cell): Grid = {
    copy(matrix.replaceCell(row, col, cell))
  }

  def placeMines(rowUsed: Int, colUsed: Int): Grid = {
    var grid = this
    val numMines = 0.5 * size * size - 8 * size + 40
    val mineNumbers = Seq.fill(numMines.toInt)(util.Random.nextInt(size * size))

    mineNumbers.map(x => (x / size, x % size)).foreach { case (row, col) =>
      if (grid.matrix.cell(row, col).value != -1 && !(row == rowUsed && col == colUsed)) {
        val checked = grid.matrix.cell(row, col).checked
        val colorBack = grid.matrix.cell(row, col).colorBack
        val flag = grid.matrix.cell(row, col).flag
        grid = setCell(grid.matrix, row, col, Cell(checked, -1, 'b', colorBack, flag))
      }
    }

    grid
  }

  def calculateValues(): Grid = {
    var grid = this

    for (i <- 0 until size; j <- 0 until size) {
      var value: Int = 0
      var row: Int = 0
      var col: Int = 0

      if (grid.matrix.cell(i, j).value != -1) {
        for (k <- 0 until 8) {
          row = i + rowArray(k)
          col = j + colArray(k)

          if (row >= 0 && col >= 0 && row < size && col < size) {
            if (grid.matrix.cell(row, col).value == -1) {
              value += 1
            }
          }
        }

        val checked = grid.matrix.cell(i, j).checked
        val color = grid.matrix.cell(i, j).color
        val colorBack = grid.matrix.cell(i, j).colorBack
        val flag = grid.matrix.cell(i, j).flag
        grid = setCell(grid.matrix, i, j, Cell(checked, value, color, colorBack, flag))
      }
    }

    grid
  }

  def setChecked(row: Int, col: Int, checked: Boolean): Grid = {
    val cell = matrix.cell(row, col)
    setCell(matrix, row, col, Cell(checked, cell.value, cell.color, cell.colorBack, cell.flag))
  }

  def setFlag(row: Int, col: Int, flag: Boolean): Grid = {
    val cell = matrix.cell(row, col)
    setCell(matrix, row, col, Cell(cell.checked, cell.value, cell.color, cell.colorBack, flag))
  }

  def setColor(row: Int, col: Int, color: Int): Grid = {
    val cell = matrix.cell(row, col)
    setCell(matrix, row, col, Cell(cell.checked, cell.value, color, cell.colorBack, cell.flag))
  }

  def setColorBack(row: Int, col: Int, colorBack: Color): Grid = {
    val cell = matrix.cell(row, col)
    setCell(matrix, row, col, Cell(cell.checked, cell.value, cell.color, Some(colorBack), cell.flag))
  }

  def rowIndex(i: Int): Int = {
    this.rowArray(i)
  }

  def colIndex(i: Int): Int = {
    this.colArray(i)
  }

  def solve(): Future[(List[(Int, Int)], GridInterface)] = {
    val s = new Solver(this)
    Future {
      s.solve()
    }
  }

  override def toString: String = {
    val lineSeparator = ("+-" + ("--" * size)) + "+\n"
    val line = ("| " + ("y " * size)) + "|\n"
    var box = "\n" + (lineSeparator + (line * size)) + lineSeparator

    for {
      row <- 0 until size
      col <- 0 until size
    } if (!matrix.cell(row, col).checked) {
      if (!matrix.cell(row, col).flag) {
        box = box.replaceFirst("y", "x")
      } else {
        box = box.replaceFirst("y", "f")
      }
    } else {
      if (matrix.cell(row, col).value == -1) {
        box = box.replaceFirst("y", "b")
      } else {
        box = box.replaceFirst("y", matrix.cell(row, col).value.toString)
      }
    }

    box
  }

}

object Grid {

  def loadGrid(height: Int, width: Int, numMines: Int, values: List[Int],
               checkedList: List[Boolean], flagList: List[Boolean], colorList: List[Int]): Grid = {
    var grid = new Grid(height)

    for (i <- 0 until height; j <- 0 until width) {
      val checked = checkedList(width - j - 1 + (height - 1 - i) * width)
      val value = values(width - j - 1 + (height - 1 - i) * width)
      val color = colorList(width - j - 1 + (height - 1 - i) * width)
      val colorBack = grid.matrix.cell(i, j).colorBack
      val flag = flagList(width - j - 1 + (height - 1 - i) * width)
      grid = grid.setCell(grid.matrix, i, j, Cell(checked, value, color, colorBack, flag))
    }

    for (i <- 0 until height; j <- 0 until width) {
      if (grid.matrix.cell(i, j).checked) {
        val checked = grid.matrix.cell(i, j).checked
        val value = grid.matrix.cell(i, j).value
        val color = grid.matrix.cell(i, j).color
        val colorBack = Some(Color.LIGHT_GRAY)
        val flag = grid.matrix.cell(i, j).flag
        grid = grid.setCell(grid.matrix, i, j, Cell(checked, value, color, colorBack, flag))
      }
    }

    grid
  }

}
