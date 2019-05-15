package de.htwg.sa.minesweeper.util

import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

class EmptyObserver extends Observer {

  override def update(): Unit = 0

}

@RunWith(classOf[JUnitRunner])
class ObservableSpec extends WordSpec with Matchers {

  "An Observable" should {
    "do everything correctly" in {
      val emptyObserver = new EmptyObserver()
      val observable = new Observable()
      val subscribers = observable.subscribers
      observable.add(emptyObserver)
      observable.remove(emptyObserver)
      observable.notifyObservers()
      observable.subscribers should be(subscribers)
    }
  }

}
