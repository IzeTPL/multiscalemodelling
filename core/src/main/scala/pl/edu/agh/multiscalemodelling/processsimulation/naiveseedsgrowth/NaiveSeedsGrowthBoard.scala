package pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth

import java.util.Random

import com.badlogic.gdx.graphics.Color
import pl.edu.agh.multiscalemodelling.engine.logic.{Board, Cell, Point, State}

object NaiveSeedsGrowthBoard {
  var newID = 0
}

class NaiveSeedsGrowthBoard(val x: Int, val y: Int) extends Board {
  size = new Point(x, y)
  var i = 0
  while ( {
    i < size.x
  }) {
    var j = 0
    while ( {
      j < size.y
    }) {
      cells.add(new NaiveSeedsGrowthCell(i, j))
      j += 1
      j - 1
    }
    i += 1
    i - 1
  }

  override def clear(): Unit = {
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      cell.nextState = State.EMPTY
      cell.nextColor = Color.BLACK
      cell.update()
    }
  }

  override def randomize(cell: Cell, random: Random): Unit = {
    super.randomize(cell, random)
    val test = cell.seedID == 0
    if ((cell.nextState eq State.ALIVE) && test) {
      var color = new Color(random.nextFloat, random.nextFloat, random.nextFloat, 1)
      while ( {
        (color.r + color.g + color.a) < 0.5f
      }) color = new Color(random.nextFloat, random.nextFloat, random.nextFloat, 1)
      cell.nextColor = color
      cell.nextState = State.ALIVE
      cell.asInstanceOf[NaiveSeedsGrowthCell].nextSeedID = {
        NaiveSeedsGrowthBoard.newID += 1
        NaiveSeedsGrowthBoard.newID
      }
      NaiveSeedsGrowthCell.getSeedList.put(
        NaiveSeedsGrowthBoard.newID,
        color
      )
      ()
    }
  }

  def swap(x: Int, y: Int): Unit = cells.get(x * size.x + y).asInstanceOf[NaiveSeedsGrowthCell].swap()

}