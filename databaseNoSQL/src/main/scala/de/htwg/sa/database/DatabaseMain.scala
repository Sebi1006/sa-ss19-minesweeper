package de.htwg.sa.database

object DatabaseMain {

  def main(args: Array[String]): Unit = {
    val database = new DatabaseSetup()
    val rest = new RestApi(database)
    rest.startServer()
}

}
