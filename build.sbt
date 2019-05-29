lazy val commonSettings = Seq(
  organization := "de.htwg.sa",
  version := "1.0.0",
  scalaVersion := "2.12.7"
)

lazy val root = (project in file("."))
  .settings(
    name := "minesweeper"
  )
  .aggregate(
    game,
    database
  )

lazy val game = project
  .settings(
    commonSettings,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    libraryDependencies += "junit" % "junit" % "4.8" % "test",
    libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.3",
    libraryDependencies += "com.google.inject" % "guice" % "4.1.0",
    libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0",
    libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6",
    libraryDependencies += "com.google.inject.extensions" % "guice-assistedinject" % "4.1.0",
    libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8",
    libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.22",
    coverageExcludedPackages := "de.htwg.sa.minesweeper.view.Gui;de.htwg.sa.minesweeper.view.RestApi;de.htwg.sa.minesweeper.model.saveandloadcomponent;de.htwg.sa.minesweeper.MineSweeper"
  )

lazy val database = project
  .settings(
    commonSettings,
    libraryDependencies ++= List(
      "com.typesafe.slick" %% "slick" % "3.2.0",
      "org.slf4j" % "slf4j-nop" % "1.7.10",
      "com.h2database" % "h2" % "1.4.187"
    ),
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6",
    libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8",
    libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.22",
    libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.0",
    libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0"
  )
