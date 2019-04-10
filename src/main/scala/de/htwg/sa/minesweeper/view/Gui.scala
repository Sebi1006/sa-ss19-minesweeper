package de.htwg.sa.minesweeper.view

import de.htwg.sa.minesweeper.controller.{CellChanged, ControllerInterface, GridSizeChanged, Winner}

import java.awt.{Dimension, _}
import java.awt.event._
import javax.swing._
import scala.swing.Frame

class Gui(controller: ControllerInterface) extends JFrame("HTWG Minesweeper") with ActionListener with ContainerListener {

  var frameWidth: Int = 10
  var frameHeight: Int = 10
  var savedHeight: Int = 10
  var savedWidth: Int = 10
  var savedNumMines: Int = 10
  var numberOfMines: Int = 10
  var detectedMines: Int = 0
  var icon: Array[ImageIcon] = new Array[ImageIcon](14)
  var blocks: Array[Array[JButton]] = _
  var panelBlock: JPanel = new JPanel()
  var panelInfo: JPanel = new JPanel()
  var tfMine: JTextField = _
  var tfTime: JTextField = _
  var resetButton: JButton = new JButton("")
  var startTimeBool: Boolean = false
  var stopWatch: StopWatch = new StopWatch()
  var mouseHandler: MouseHandler = _
  var var1: Int = _
  var var2: Int = _

  setMenu()
  setLocation(600, 300)
  setImageIcon()
  controller.createGrid(10)
  setPanel(10, 10)
  var guiReactor = new GuiReactor(controller)

  def setMenu(): Unit = {
    val bar: JMenuBar = new JMenuBar()
    val file: JMenu = new JMenu("File")
    val menuitem: JMenuItem = new JMenuItem("New Game")
    val save: JMenuItem = new JMenuItem("Save")
    val load: JMenuItem = new JMenuItem("Load")
    val exit: JMenuItem = new JMenuItem("Exit")
    val game: JMenu = new JMenu("Game")
    val beginner: JCheckBoxMenuItem = new JCheckBoxMenuItem("Beginner")
    val intermediate: JCheckBoxMenuItem = new JCheckBoxMenuItem("Intermediate")
    val expert: JCheckBoxMenuItem = new JCheckBoxMenuItem("Expert")
    val edit: JMenu = new JMenu("Edit")
    val undo: JMenuItem = new JMenuItem("Undo")
    val redo: JMenuItem = new JMenuItem("Redo")
    val solve: JMenuItem = new JMenuItem("Solve")
    val status: ButtonGroup = new ButtonGroup()

    menuitem.addActionListener((e: ActionEvent) => {
      controller.createGrid(10)
      numberOfMines = 10
      setPanel(10, 10)
      beginner.setSelected(true)
    })

    beginner.addActionListener((e: ActionEvent) => {
      panelBlock.removeAll()
      reset()
      controller.createGrid(10)
      numberOfMines = 10
      setPanel(10, 10)
      savedHeight = 10
      savedWidth = 10
      savedNumMines = 10
      panelBlock.revalidate()
      panelBlock.repaint()
      beginner.setSelected(true)
    })

    intermediate.addActionListener((e: ActionEvent) => {
      panelBlock.removeAll()
      reset()
      controller.createGrid(16)
      numberOfMines = 40
      setPanel(16, 16)
      savedHeight = 16
      savedWidth = 16
      savedNumMines = 40
      panelBlock.revalidate()
      panelBlock.repaint()
      intermediate.setSelected(true)
    })

    expert.addActionListener((e: ActionEvent) => {
      panelBlock.removeAll()
      reset()
      controller.createGrid(20)
      numberOfMines = 80
      setPanel(20, 20)
      savedHeight = 20
      savedWidth = 20
      savedNumMines = 80
      panelBlock.revalidate()
      panelBlock.repaint()
      expert.setSelected(true)
    })

    exit.addActionListener((e: ActionEvent) => {
      System.exit(0)
    })

    undo.addActionListener((e: ActionEvent) => {
      controller.undo()
    })

    redo.addActionListener((e: ActionEvent) => {
      controller.redo()
    })

    solve.addActionListener((e: ActionEvent) => {
      controller.solve()
    })

    save.addActionListener((e: ActionEvent) => {
      controller.save()
    })

    load.addActionListener((e: ActionEvent) => {
      controller.load()
    })

    setJMenuBar(bar)
    file.add(menuitem)
    file.addSeparator()
    file.add(save)
    file.add(load)
    file.addSeparator()
    file.add(exit)
    bar.add(file)
    status.add(beginner)
    status.add(intermediate)
    status.add(expert)
    game.add(beginner)
    game.add(intermediate)
    game.add(expert)
    bar.add(game)
    edit.add(undo)
    edit.add(redo)
    edit.addSeparator()
    edit.add(solve)
    bar.add(edit)
  }

  def setPanel(height: Int, width: Int): Unit = {
    frameWidth = 20 * width
    frameHeight = 30 * height

    setSize(frameWidth, frameHeight)
    setResizable(false)

    detectedMines = numberOfMines
    blocks = Array.ofDim[JButton](controller.grid.size, controller.grid.size)
    mouseHandler = new MouseHandler()

    getContentPane.removeAll()
    panelBlock.removeAll()

    tfMine = new JTextField("" + numberOfMines, 3)
    tfMine.setEditable(false)
    tfMine.setFont(new Font("DigtalFont.TTF", Font.BOLD, 25))
    tfMine.setBackground(Color.BLACK)
    tfMine.setForeground(Color.RED)
    tfMine.setBorder(BorderFactory.createLoweredBevelBorder())

    tfTime = new JTextField("000", 3)
    tfTime.setEditable(false)
    tfTime.setFont(new Font("DigtalFont.TTF", Font.BOLD, 25))
    tfTime.setBackground(Color.BLACK)
    tfTime.setForeground(Color.RED)
    tfTime.setBorder(BorderFactory.createLoweredBevelBorder())

    resetButton.setIcon(icon(11))
    resetButton.setBorder(BorderFactory.createLoweredBevelBorder())

    panelInfo.removeAll()
    panelInfo.setLayout(new BorderLayout())
    panelInfo.add(tfMine, BorderLayout.WEST)
    panelInfo.add(resetButton, BorderLayout.CENTER)
    panelInfo.add(tfTime, BorderLayout.EAST)
    panelInfo.setBorder(BorderFactory.createLoweredBevelBorder())

    panelBlock.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(10, 10, 10, 10),
        BorderFactory.createLoweredBevelBorder()))
    panelBlock.setPreferredSize(new Dimension(frameWidth, frameHeight))
    panelBlock.setLayout(new GridLayout(0, controller.grid.size))
    panelBlock.addContainerListener(this)

    for (i <- 0 until controller.grid.size; j <- 0 until controller.grid.size) {
      blocks(i)(j) = new JButton("")
      blocks(i)(j).addMouseListener(mouseHandler)
      panelBlock.add(blocks(i)(j))
    }

    reset()
    panelBlock.revalidate()
    panelBlock.repaint()

    getContentPane.setLayout(new BorderLayout())
    getContentPane.addContainerListener(this)
    getContentPane.repaint()
    getContentPane.add(panelBlock, BorderLayout.CENTER)
    getContentPane.add(panelInfo, BorderLayout.NORTH)

    setVisible(true)
  }

  resetButton.addActionListener((e: ActionEvent) => {
    try {
      if (!startTimeBool) {
        stopWatch.start()
        startTimeBool = true
      }

      stopWatch.stop()
      startTimeBool = false
      controller.createGrid(savedHeight)
      setPanel(savedHeight, savedWidth)
    } catch {
      case ex: Exception => {
        if (!startTimeBool) {
          stopWatch.start()
          startTimeBool = true
        }

        stopWatch.stop()
        startTimeBool = false
        controller.createGrid(savedHeight)
        setPanel(savedHeight, savedWidth)
      }
    }

    reset()
  })

  def reset(): Unit = {
    if (startTimeBool) {
      startTimeBool = false
      stopWatch.stop()
    }

    for (i <- 0 until controller.grid.size; j <- 0 until controller.grid.size) {
      controller.grid.setColor(i, j, 'w')
    }
  }

  def winner(win: Boolean): Unit = {
    if (win) {
      for (i <- 0 until controller.grid.size; j <- 0 until controller.grid.size) {
        blocks(i)(j).removeMouseListener(mouseHandler)
      }

      stopWatch.stop()
      resetButton.setIcon(icon(13))
      JOptionPane.showMessageDialog(this, "Hurray! You win!")
    } else {
      for (i <- 0 until controller.grid.size; j <- 0 until controller.grid.size) {
        blocks(i)(j).removeMouseListener(mouseHandler)
      }

      paint()
      stopWatch.stop()
      resetButton.setIcon(icon(12))
      JOptionPane.showMessageDialog(this, "Game Over!")
    }
  }

  def updateTextfield(): Unit = {
    if (controller.mineFound >= 0) {
      tfMine.setText("" + controller.mineFound)
      paint()
    } else {
      tfMine.setText("" + 0)
      paint()
    }
  }

  def setImageIcon(): Unit = {
    val projectPath = System.getProperty("user.dir")
    var name: String = ""
    var i: Int = 0

    while (i <= 8) {
      name = projectPath + "\\src\\main\\resources\\" + i + ".png"
      icon(i) = new ImageIcon(name) {
        i += 1
      }
    }

    icon(9) = new ImageIcon(projectPath + "\\src\\main\\resources\\mine.png")
    icon(10) = new ImageIcon(projectPath + "\\src\\main\\resources\\flag.png")
    icon(11) = new ImageIcon(projectPath + "\\src\\main\\resources\\new game.png")
    icon(12) = new ImageIcon(projectPath + "\\src\\main\\resources\\lose.png")
    icon(13) = new ImageIcon(projectPath + "\\src\\main\\resources\\win.png")
  }

  def paint(): Unit = {
    for (i <- 0 until controller.grid.size; j <- 0 until controller.grid.size) {
      if (controller.grid.matrix.cell(i, j).checked) {
        if (controller.grid.matrix.cell(i, j).colorBack.contains(Color.LIGHT_GRAY)) {
          blocks(i)(j).setBackground(Color.LIGHT_GRAY)
        }

        if (controller.grid.matrix.cell(i, j).value == -1) {
          blocks(i)(j).setIcon(icon(9))
        } else {
          blocks(i)(j).setIcon(icon(controller.grid.matrix.cell(i, j).value))
        }
      } else if (controller.grid.matrix.cell(i, j).flag) {
        blocks(i)(j).setIcon(icon(10))
      } else {
        blocks(i)(j).setBackground(null)
        blocks(i)(j).setIcon(null)
      }
    }
  }

  def componentAdded(ce: ContainerEvent): Unit = {}

  def componentRemoved(ce: ContainerEvent): Unit = {}

  def actionPerformed(ae: ActionEvent): Unit = {}

  class MouseHandler extends MouseAdapter {

    override def mouseClicked(me: MouseEvent): Unit = {
      for (i <- 0 until controller.grid.size; j <- 0 until controller.grid.size
           if me.getSource == blocks(i)(j)) {
        var1 = i
        var2 = j
      }

      if (me.getButton == MouseEvent.BUTTON1) {
        controller.setChecked(var1, var2, false, true, false)
      } else {
        if (controller.grid.matrix.cell(var1, var2).flag) {
          controller.setFlag(var1, var2, true, true)
        } else {
          controller.setFlag(var1, var2, false, true)
        }
      }
    }

  }

  class StopWatch extends JFrame with Runnable {

    var startTime: Long = _
    var updater: Thread = _
    var isRunning: Boolean = false
    var i: Long = 0
    var displayUpdater: Runnable = () => {
      displayElapsedTime(i)
      i += 1
    }

    def stop(): Unit = {
      val elapsed: Long = i
      isRunning = false

      try updater.join()
      catch {
        case ie: InterruptedException => {
          ie.printStackTrace(System.err)
        }
      }

      displayElapsedTime(elapsed)
      i = 0
    }

    private def displayElapsedTime(elapsedTime: Long): Unit = {
      if (elapsedTime >= 0 && elapsedTime < 9) {
        tfTime.setText("00" + elapsedTime)
      } else if (elapsedTime > 9 && elapsedTime < 99) {
        tfTime.setText("0" + elapsedTime)
      } else if (elapsedTime > 99 && elapsedTime < 999) {
        tfTime.setText("" + elapsedTime)
      }
    }

    def run(): Unit = {
      try while (isRunning) {
        SwingUtilities.invokeAndWait(displayUpdater)
        Thread.sleep(1000)
      } catch {
        case ite: java.lang.reflect.InvocationTargetException => {
          ite.printStackTrace(System.err)
        }
        case ie: InterruptedException => {
          ie.printStackTrace(System.err)
        }
      }
    }

    def start(): Unit = {
      startTime = System.currentTimeMillis()
      isRunning = true
      updater = new Thread(this)
      updater.start()
    }

  }

  class GuiReactor(controller: ControllerInterface) extends Frame {

    var startTime = false
    listenTo(controller)

    reactions += {
      case event: GridSizeChanged => resize(event.height, event.width, event.mineNumber)
      case event: CellChanged => repaintGrid()
      case event: Winner => winner(event.win)
    }

    def resize(height: Int, width: Int, mineNumber: Int): Unit = {
      panelBlock.removeAll()
      reset()
      numberOfMines = mineNumber
      setPanel(height, width)
      savedHeight = height
      savedWidth = width
      savedNumMines = mineNumber
      panelBlock.revalidate()
      panelBlock.repaint()
    }

    def repaintGrid(): Unit = {
      if (!startTimeBool) {
        stopWatch.start()
        startTimeBool = true
      }

      updateTextfield()
    }

  }

}
