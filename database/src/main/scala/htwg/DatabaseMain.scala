package hwtg

object DatabaseMain {
  val database = new DatabaseSetup
  val rest = new RestApi(database)

  def main(args: Array[String]): Unit = {
    var input: String = ""
    rest.startRestApi()

    do {
      val result = scala.io.Source.fromURL("http://localhost:8887/loadGrid").mkString
      print(result)
      input = readLine()
    } while (input != "exit")
  }

}
