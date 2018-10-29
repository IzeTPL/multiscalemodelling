package pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth

import pl.edu.agh.multiscalemodelling.engine.logic.Logic

class NaiveSeedsGrowthLogic(val x: Int, val y: Int) extends Logic {
  board = new NaiveSeedsGrowthBoard(x, y)

  override def click(x: Int, y: Int): Unit = board.asInstanceOf[NaiveSeedsGrowthBoard].swap(x, y)
}