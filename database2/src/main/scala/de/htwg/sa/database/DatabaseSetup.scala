package de.htwg.sa.database

import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala._
import org.mongodb.scala.model.Filters

class DatabaseSetup {

  val uri: String = "mongodb://localhost:27017"
  val client: MongoClient = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase("saveLoad")
  val collection: MongoCollection[Document] = db.getCollection("saves")

  def save(gridJson: String): Unit = {
    val document: Document = Document("_id" -> 1, "save" -> gridJson)
    val insertObservable: Observable[Completed] = collection.insertOne(document)

    insertObservable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = println("onComplete")
    })

    collection.replaceOne(Filters.eq("_id", 1), document).subscribe((updateResult: UpdateResult) => println(updateResult))
  }

  def load(): String = {
    var loadedString = "MS"

    collection.find().collect().subscribe((results: Seq[Document]) => {
      for (document <- results.iterator) {
        val save = document.get("save")

        save match {
          case Some(save) => loadedString = save.asString().getValue
          case None => loadedString = ""
        }
      }
    })

    while (loadedString.equals("MS")) {
      Thread.sleep(10)
    }

    loadedString
  }

  def delete(): Unit = {
    collection.deleteOne(Filters.eq("_id", 1)).subscribe((deleteResult: DeleteResult) => println(deleteResult))
  }

  def update(gridJson: String): Unit = {
    val document: Document = Document("save" -> gridJson)
    collection.updateOne(Filters.eq("_id", 1), document).subscribe((updateResult: UpdateResult) => println(updateResult))
  }

}
