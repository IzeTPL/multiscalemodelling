package pl.edu.agh.multiscalemodelling.engine.logic

import java.util

import pl.edu.agh.multiscalemodelling.engine.logic.enumeration.{OperationMode, State}
import pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth.NaiveSeedsGrowthCell

class LogicThread(var cells: util.List[NaiveSeedsGrowthCell]) extends Thread {
  override def run(): Unit = {
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      cell.checkNeighbors(OperationMode.SIMPLE_GROWTH)
    }
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      cell.update()
      if(cell.nextState == State.EMPTY) Logic.allProcessed = false
    }
  }
}