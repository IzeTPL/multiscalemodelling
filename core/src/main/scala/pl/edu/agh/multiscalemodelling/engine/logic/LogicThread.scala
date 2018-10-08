package pl.edu.agh.multiscalemodelling.engine.logic

import java.util

class LogicThread(var cells: util.List[Cell]) extends Thread {
  override def run(): Unit = {
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      cell.checkNeighbors
    }
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      cell.update()
    }
  }
}