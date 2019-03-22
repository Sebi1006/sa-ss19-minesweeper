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
  controller.createGrid(10, 10, 10)
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
    val custom: JCheckBoxMenuItem = new JCheckBoxMenuItem("Custom...")
    val edit: JMenu = new JMenu("Edit")
    val undo: JMenuItem = new JMenuItem("Undo")
    val redo: JMenuItem = new JMenuItem("Redo")
    val solve: JMenuItem = new JMenuItem("Solve")
    val status: ButtonGroup = new ButtonGroup()

    menuitem.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        controller.createGrid(10, 10, 10)
        numberOfMines = 10
        setPanel(10, 10)
      }
    })

    beginner.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        panelBlock.removeAll()
        reset()
        controller.createGrid(10, 10, 10)
        numberOfMines = 10
        setPanel(10, 10)
        savedHeight = 10
        savedWidth = 10
        savedNumMines = 10
        panelBlock.revalidate()
        panelBlock.repaint()
        beginner.setSelected(true)
      }
    })

    intermediate.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        panelBlock.removeAll()
        reset()
        controller.createGrid(16, 16, 70)
        numberOfMines = 70
        setPanel(16, 16)
        savedHeight = 16
        savedWidth = 16
        savedNumMines = 70
        panelBlock.revalidate()
        panelBlock.repaint()
        intermediate.setSelected(true)
      }
    })

    expert.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        panelBlock.removeAll()
        reset()
        controller.createGrid(20, 20, 150)
        numberOfMines = 150
        setPanel(20, 20)
        savedHeight = 20
        savedWidth = 20
        savedNumMines = 150
        panelBlock.revalidate()
        panelBlock.repaint()
        expert.setSelected(true)
      }
    })

    custom.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        panelBlock.removeAll()
        reset()
        var c: Custom = new Custom()
        panelBlock.revalidate()
        panelBlock.repaint()
        custom.setSelected(true)
      }
    })

    exit.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        System.exit(0)
      }
    })

    undo.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        controller.undo()
      }
    })

    redo.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        controller.redo()
      }
    })

    solve.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        controller.solve()
      }
    })

    save.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        controller.save()
      }
    })

    load.addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {
        controller.load()
      }
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
    status.add(custom)
    game.add(beginner)
    game.add(intermediate)
    game.add(expert)
    game.add(custom)
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
    blocks = Array.ofDim[JButton](controller.height(), controller.width())
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
    panelBlock.setLayout(new GridLayout(0, controller.width()))
    panelBlock.addContainerListener(this)

    for (i <- 0 until controller.height(); j <- 0 until controller.width()) {
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

  resetButton.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent): Unit = {
      try {
        if (!startTimeBool) {
          stopWatch.start()
          startTimeBool = true
        }

        stopWatch.stop()
        startTimeBool = false
        controller.createGrid(savedHeight, savedWidth, savedNumMines)
        setPanel(savedHeight, savedWidth)
      } catch {
        case ex: Exception => {
          if (!startTimeBool) {
            stopWatch.start()
            startTimeBool = true
          }

          stopWatch.stop()
          startTimeBool = false
          controller.createGrid(savedHeight, savedWidth, savedNumMines)
          setPanel(savedHeight, savedWidth)
        }
      }

      reset()
    }
  })

  def reset(): Unit = {
    if (startTimeBool) {
      startTimeBool = false
      stopWatch.stop()
    }

    for (i <- 0 until controller.height(); j <- 0 until controller.width()) {
      controller.setColor(i, j, 'w')
    }
  }

  def winner(win: Boolean): Unit = {
    if (win) {
      for (i <- 0 until controller.height(); j <- 0 until controller.width()) {
        blocks(i)(j).removeMouseListener(mouseHandler)
      }

      stopWatch.stop()
      resetButton.setIcon(icon(13))
      JOptionPane.showMessageDialog(this, "Hurray! You win!")
    } else {
      for (i <- 0 until controller.height(); j <- 0 until controller.width()) {
        if (controller.getMine(i, j)) {
          blocks(i)(j).removeMouseListener(mouseHandler)
        }
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
        i - 1
      }
    }

    icon(9) = new ImageIcon(projectPath + "\\src\\main\\resources\\mine.png")
    icon(10) = new ImageIcon(projectPath + "\\src\\main\\resources\\flag.png")
    icon(11) = new ImageIcon(projectPath + "\\src\\main\\resources\\new game.png")
    icon(12) = new ImageIcon(projectPath + "\\src\\main\\resources\\lose.png")
    icon(13) = new ImageIcon(projectPath + "\\src\\main\\resources\\win.png")
  }

  def paint(): Unit = {
    for (i <- 0 until controller.height(); j <- 0 until controller.width()) {
      if (controller.getChecked(i, j)) {
        if (controller.getColorBack(i, j) == Color.LIGHT_GRAY) {
          blocks(i)(j).setBackground(Color.LIGHT_GRAY)
        }

        if (controller.getValue(i, j) == -1) {
          blocks(i)(j).setIcon(icon(9))
        } else {
          blocks(i)(j).setIcon(icon(controller.getValue(i, j)))
        }
      } else if (controller.getFlag(i, j)) {
        blocks(i)(j).setIcon(icon(10))
      } else {
        blocks(i)(j).setIcon(null)
        blocks(i)(j).setBackground(null)
      }
    }
  }

  def componentAdded(ce: ContainerEvent): Unit = {}

  def componentRemoved(ce: ContainerEvent): Unit = {}

  def actionPerformed(ae: ActionEvent): Unit = {}

  class MouseHandler extends MouseAdapter {

    override def mouseClicked(me: MouseEvent): Unit = {
      for (i <- 0 until controller.height(); j <- 0 until controller.width()
           if me.getSource == blocks(i)(j)) {
        var1 = i
        var2 = j
      }

      if (me.getButton == MouseEvent.BUTTON1) {
        controller.setChecked(var1, var2, false, true, false)
      } else {
        if (controller.getFlag(var1, var2)) {
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
    var displayUpdater: Runnable = new Runnable() {
      def run(): Unit = {
        displayElapsedTime(i)
        i += 1
      }
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

  class Custom extends JFrame("Custom Field") with ActionListener {

    var tf1: JTextField = new JTextField()
    var tf2: JTextField = new JTextField()
    var tf3: JTextField = new JTextField()
    var l1: JLabel = new JLabel("Height")
    var l2: JLabel = new JLabel("Width")
    var l3: JLabel = new JLabel("Mines")
    var b1: JButton = new JButton("OK")
    var b2: JButton = new JButton("Cancel")
    var i1: Int = _
    var i2: Int = _
    var i3: Int = _

    setSize(180, 200)
    setResizable(false)
    setLocation(900, 300)

    b1.addActionListener(this)
    b2.addActionListener(this)

    getContentPane.setLayout(new GridLayout(0, 2))
    getContentPane.add(l1)
    getContentPane.add(tf1)
    getContentPane.add(l2)
    getContentPane.add(tf2)
    getContentPane.add(l3)
    getContentPane.add(tf3)
    getContentPane.add(b1)
    getContentPane.add(b2)

    setVisible(true)

    def actionPerformed(e: ActionEvent): Unit = {
      if (e.getSource == b1) {
        try {
          i1 = java.lang.Integer.parseInt(tf1.getText)
          i2 = java.lang.Integer.parseInt(tf2.getText)
          i3 = java.lang.Integer.parseInt(tf3.getText)

          if (i3 >= i1 * i2) {
            JOptionPane.showMessageDialog(this, "Number of Mines must be smaller than grid size")
            return
          } else if (i1 < 10 || i2 < 10 || i3 < 10) {
            JOptionPane.showMessageDialog(this, "Height, Width and Number of Mines must be minimum 10")
            return
          } else if (i1 > 35 || i2 > 35) {
            JOptionPane.showMessageDialog(this, "Height and Width may not exceed 35")
            return
          }

          controller.createGrid(i1, i2, i3)
          savedHeight = i1
          savedWidth = i2
          savedNumMines = i3
          numberOfMines = i3
          setPanel(i1, i2)
          dispose()
        } catch {
          case ex: Exception => {
            JOptionPane.showMessageDialog(this, "Integer!")
            tf1.setText("")
            tf2.setText("")
            tf3.setText("")
          }
        }
      }

      if (e.getSource == b2) {
        dispose()
      }
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
