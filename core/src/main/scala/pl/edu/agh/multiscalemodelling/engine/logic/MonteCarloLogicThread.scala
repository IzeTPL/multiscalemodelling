package pl.edu.agh.multiscalemodelling.engine.logic

import java.util
import java.util.Random
import java.util.function.Predicate

import pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth.NaiveSeedsGrowthCell

class MonteCarloLogicThread(val cells: util.List[NaiveSeedsGrowthCell]) extends Thread {

  override def run(): Unit = {
    val cells = new util.ArrayList[NaiveSeedsGrowthCell](this.cells)
    import scala.collection.JavaConversions._
    cells.removeIf(
      new Predicate[NaiveSeedsGrowthCell] {
      override def test(cell: NaiveSeedsGrowthCell): Boolean = cell.toRemove
    })

    val random = new Random
    while ( {
      cells.size > 0
    }) {
      val index = random.nextInt(cells.size)
      cells.get(index).checkNeighbors(OperationMode.SIMPLE_MCS)
      cells.remove(index)
    }
    import scala.collection.JavaConversions._
    for (cell <- this.cells) {
      cell.update()
      if(cell.nextState == State.EMPTY) Logic.allProcessed = false
    }
  }
}