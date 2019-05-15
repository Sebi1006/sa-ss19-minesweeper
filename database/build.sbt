name := "database"
scalaVersion := "2.12.8"

mainClass in Compile := Some("main")

libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "3.2.0",
  "org.slf4j" % "slf4j-nop" % "1.7.10",
  "com.h2database" % "h2" % "1.4.187"
)

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8"

libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.22"

libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.0"

libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0"

fork in run := true
