[![Build Status](https://travis-ci.org/Sebi1006/sa-ss19-minesweeper.svg?branch=master)](https://travis-ci.org/Sebi1006/sa-ss19-minesweeper) - [![Coverage Status](https://coveralls.io/repos/github/Sebi1006/sa-ss19-minesweeper/badge.svg?branch=master)](https://coveralls.io/github/Sebi1006/sa-ss19-minesweeper?branch=master) - Master


# HTWG Minesweeper in Scala

This is an implementation of Minesweeper in Scala for the class
Software Architecture at the University of Applied Sciences HTWG Konstanz.

* Has a folder structure prepared for an application build in MVC style
* Has *ScalaTest* and *ScalaMock* at their latest versions as dependencies
* Has *sbt-scalariform*, *sbt-scapegoat*, *scalastyle-sbt-plugin* and *sbt-scoverage* sbt plugins
* Has .gitignore defaults


## Commands to play

* **h** for help
* **new** to start a new game
* **exit** to quit the game
* **1** for beginner grid
* **2** for advanced grid
* **3** for expert grid
* **(row) (column)** to set a cell
* **f (row) (column)** to set a flag
* **s** to solve the current game
* **save** to save the current grid
* **load** to load the last saved grid
