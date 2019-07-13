package de.htwg.sa.database

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.scaladsl.Sink
import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

class RestApi(database: DatabaseSetup) {

  implicit val actorSystem: ActorSystem = ActorSystem("DatabaseSystem")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  def startServer(): Unit = {
    val serverSource = Http().bind(interface = "localhost", port = 8889)

    val requestHandler: HttpRequest => HttpResponse = {
      case HttpRequest(GET, Uri.Path("/save"), _, _, _) => {
        val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
          method = HttpMethods.GET,
          uri = "http://localhost:9090/getJsonGrid"
        ))

        responseFuture.onComplete {
          case Success(value) => {
            val tmp: Future[String] = value.entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8"))
            tmp.onComplete {
              case Success(grid) => database.save(grid)
              case Failure(e) => println(e)
            }
          }
          case Failure(e) => println(e)
        }

        HttpResponse(entity = "saved")
      }

      case HttpRequest(GET, Uri.Path("/update"), _, _, _) => {
        val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(
          method = HttpMethods.GET,
          uri = "http://localhost:9090/getJsonGrid"
        ))

        responseFuture.onComplete {
          case Success(value) => {
            val tmp: Future[String] = value.entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8"))
            tmp.onComplete {
              case Success(grid) => database.update(grid)
              case Failure(e) => println(e)
            }
          }
          case Failure(e) => println(e)
        }

        HttpResponse(entity = "updated")
      }

      case HttpRequest(GET, Uri.Path("/load"), _, _, _) => {
        val response = database.load()
        HttpResponse(entity = response)
      }

      case HttpRequest(GET, Uri.Path("/delete"), _, _, _) => {
        database.delete()
        HttpResponse(entity = "deleted")
      }
    }

    serverSource.to(Sink.foreach { connection => connection handleWithSyncHandler requestHandler }).run()
  }

}
