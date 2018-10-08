package pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth

import com.badlogic.gdx.graphics.Color
import pl.edu.agh.multiscalemodelling.engine.logic.Board
import pl.edu.agh.multiscalemodelling.engine.logic.Cell
import pl.edu.agh.multiscalemodelling.engine.logic.Point
import pl.edu.agh.multiscalemodelling.engine.logic.State
import java.util.Random

object NaiveSeedsGrowthBoard {
  var newID = 0
}

class NaiveSeedsGrowthBoard(val x: Int, val y: Int) extends Board {
  size_$eq(new Point(x, y))
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
      cell.setNextState(State.EMPTY)
      cell.setNextColor(Color.BLACK)
      cell.update()
    }
  }

  override def randomize(cell: Cell, random: Random): Unit = {
    super.randomize(cell, random)
    val test = cell.getSeedID == 0
    if ((cell.getNextState eq State.ALIVE) && test) {
      var color = new Color(random.nextFloat, random.nextFloat, random.nextFloat, 1)
      while ( {
        (color.r + color.g + color.a) < 0.5f
      }) color = new Color(random.nextFloat, random.nextFloat, random.nextFloat, 1)
      cell.setNextColor(color)
      cell.setNextState(State.ALIVE)
      cell.asInstanceOf[NaiveSeedsGrowthCell].setNextSeedID({
        NaiveSeedsGrowthBoard.newID += 1;
        NaiveSeedsGrowthBoard.newID
      })
      NaiveSeedsGrowthCell.getSeedList.put(NaiveSeedsGrowthBoard.newID, color)
    }
  }

  def swap(x: Int, y: Int): Unit = cells.get(x * size.x + y).asInstanceOf[NaiveSeedsGrowthCell].swap()

  def seed(distance: Int): Unit = {
    val random = new Random
    var distanceY = 0
    var i = 0
    while ( {
      i < size.x
    }) {
      var distanceX = 0
      if (distanceY == distance + 1) distanceY = 0
      var j = 0
      while ( {
        j < size.y
      }) {
        if (distanceX == distance + 1) distanceX = 0
        if (distanceX == distance && distanceY == distance) {
          val cell = cells.get(i * size.x + j)
          cell.setNextState(State.ALIVE)
          cell.asInstanceOf[NaiveSeedsGrowthCell].setSeedID({
            NaiveSeedsGrowthBoard.newID += 1;
            NaiveSeedsGrowthBoard.newID
          })
          cell.update()
          if (cell.getCurrentState eq State.ALIVE) cell.setColor(new Color(random.nextFloat, random.nextFloat, random.nextFloat, 1))
        }
        distanceX += 1
          j += 1
          j - 1

      }
      distanceY += 1
        i += 1
        i - 1

    }
  }

  def radiusSeed(radius: Int): Unit = {
    val random = new Random
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      var inside = false
      var x = cell.getPosition.x - radius
      while ( {
        x < cell.getPosition.x + radius
      }) {
        val yspan = radius * Math.sin(Math.acos((cell.getPosition.x - x) / radius)).round.toInt
        var y = cell.getPosition.y - yspan
        while ( {
          y < cell.getPosition.y + yspan
        }) {
          val position = new Point(x, y)
          if (position.x < 0) position.x_$eq(size.x - 1)
          if (position.y < 0) position.y_$eq(size.y - 1)
          if (position.x >= size.x) position.x_$eq(0)
          if (position.y >= size.y) position.y_$eq(0)
          if (cells.get(position.x * size.x + position.y).getCurrentState eq State.ALIVE) inside = true
          if (inside) {
            y += 1
            y - 1
          }
          if (inside) {
            x += 1
            x - 1
          }
          if (!inside) randomize(cell, random)
        }
      }
    }
  }
}