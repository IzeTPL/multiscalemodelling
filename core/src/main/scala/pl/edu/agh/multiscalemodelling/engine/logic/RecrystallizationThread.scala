package pl.edu.agh.multiscalemodelling.engine.logic

import java.util
import java.util.Random

import pl.edu.agh.multiscalemodelling.engine.logic.enumeration.{OperationMode, State}
import pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth.NaiveSeedsGrowthCell

class RecrystallizationThread(val cells: util.List[NaiveSeedsGrowthCell]) extends Thread{

  override def run(): Unit = {

    val cells = new util.ArrayList[NaiveSeedsGrowthCell](this.cells)

    val random = new Random

    while ( {
      cells.size > 0
    }) {
      val index = random.nextInt(cells.size)
      cells.get(index).checkNeighbors(OperationMode.RECRYSTALLIZATION_MCS)
      cells.remove(index)
    }

    import scala.collection.JavaConversions._
    for (cell <- this.cells) {
      cell.update()
      if(cell.nextState == State.ALIVE) Logic.allProcessed = false
    }

  }

}
