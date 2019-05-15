package hwtg

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

import scala.io.StdIn

class RestApi(database: DatabaseSetup) {

  def startRestApi(): Unit = {
    implicit val actorSystem: ActorSystem = ActorSystem("DatabaseSystem")
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val route =
      path("loadGrid" / IntNumber) { id =>
        get {
          val savedGrid = database.load(id)
          print(savedGrid)
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, savedGrid)))
        }
      } ~ parameters('save, 'gridJson) { (save, gridJson) =>
        if (save == "saveGrid") {
          database.save(gridJson)
        }
        complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, gridJson)))
      }
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8887)

    println("Server online at http://localhost:8887/")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => actorSystem.terminate())
  }

}
