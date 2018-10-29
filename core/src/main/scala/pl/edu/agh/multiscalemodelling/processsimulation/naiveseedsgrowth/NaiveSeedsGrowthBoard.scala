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

/*  def seed(distance: Int): Unit = {
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
          cell.nextState = State.ALIVE
          cell.asInstanceOf[NaiveSeedsGrowthCell].nextSeedID = {
            NaiveSeedsGrowthBoard.newID += 1
            NaiveSeedsGrowthBoard.newID
          }
          cell.update()
          if (cell.currentState eq State.ALIVE) cell.color = new Color(random.nextFloat, random.nextFloat, random.nextFloat, 1)
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
      var x = cell.position.x - radius
      while ( {
        x < cell.position.x + radius && !inside
      }) {
        val yspan = radius * Math.sin(Math.acos((cell.position.x - x) / radius)).round.toInt
        var y = cell.position.y - yspan
        while ( {
          y < cell.position.y + yspan && !inside
        }) {
          val position = new Point(x, y)

          if (position.x < 0) position.x_$eq(size.x - 1)
          if (position.y < 0) position.y_$eq(size.y - 1)
          if (position.x >= size.x) position.x_$eq(0)
          if (position.y >= size.y) position.y_$eq(0)
          if (cells.get(position.x * size.x + position.y).currentState eq State.ALIVE) inside = true
          y += 1
          y - 1
        }
        x += 1
        x - 1
      }
      if (!inside) randomize(cell, random)
    }

  }*/

}