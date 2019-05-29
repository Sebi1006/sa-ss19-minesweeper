package de.htwg.sa.minesweeper.view

import de.htwg.sa.minesweeper.controller.ControllerInterface

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

class RestApi(controller: ControllerInterface) {

  implicit val actorSystem: ActorSystem = ActorSystem("MinesweeperSystem")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  def startRestApi(): Unit = {
    val route =
      path("createGrid" / IntNumber) { size =>
        get {
          controller.createGrid(size)
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, controller.getJsonGrid().toString)))
        }
      } ~ path("setChecked" / IntNumber / IntNumber) { (row, col) =>
        get {
          controller.setChecked(row, col, false, true, false)
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, controller.getJsonGrid().toString)))
        }
      } ~ path("setFlag" / IntNumber / IntNumber) { (row, col) =>
        get {
          controller.setFlag(row, col, false, true)
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, controller.getJsonGrid().toString)))
        }
      } ~ path("undo") {
        get {
          controller.undo()
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, controller.getJsonGrid().toString)))
        }
      } ~ path("redo") {
        get {
          controller.redo()
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, controller.getJsonGrid().toString)))
        }
      } ~ path("solve") {
        get {
          controller.solve()
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, controller.getJsonGrid().toString)))
        }
      } ~ path("save") {
        get {
          controller.save()
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, controller.getJsonGrid().toString)))
        }
      } ~ path("load") {
        get {
          controller.load()
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, controller.getJsonGrid().toString)))
        }
      } ~ path("getJsonGrid") {
        get {
          val json: Object = controller.getJsonGrid()
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, json.toString)))
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 9090)

    println("Server online at http://localhost:9090/")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => actorSystem.terminate())
  }

}
