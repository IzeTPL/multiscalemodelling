package pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth

import com.badlogic.gdx.graphics.Color
import pl.edu.agh.multiscalemodelling.engine.logic.{Cell, State}

import scala.collection.mutable
import scala.util.Random

object NaiveSeedsGrowthCell {

  var seedList: mutable.HashMap[Int, Color] = _
  def getSeedList: mutable.HashMap[Int, Color] = seedList

}

class NaiveSeedsGrowthCell(x: Int, y: Int) extends Cell(x, y) {

  NaiveSeedsGrowthCell.seedList = new mutable.HashMap[Int, Color]()
  seedID = 0
  nextColor = new Color(Color.BLACK)

  protected var nextSeedID: Integer = _

  override def checkNeighbors: Boolean = currentState match {

    case State.EMPTY => {

      val neighborSeeds = new mutable.HashMap[Int, Int]

      import scala.collection.JavaConversions._
      for (cell: Cell <- neighbors) {
        if (neighborSeeds.contains(cell.getSeedID)) {

          neighborSeeds.update(cell.getSeedID, neighborSeeds.getOrElse(cell.getSeedID, 0) + 1)

        } else {

          neighborSeeds.put(cell.getSeedID, 1)

        }
      }

      var max = -1

      neighborSeeds.foreach { case (key, value) => {
        if (value > max && (key != 0)) {
          max = value
          nextSeedID = key
        }
      }
      }

      if (max != -1) {

        nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
        nextState = State.ALIVE

      }

      true

    }

    case _ => false

  }

  override def update(): Unit = {

    (currentState, nextState) match {

      case (State.EMPTY, State.ALIVE) => {

        color = nextColor
        seedID = nextSeedID

      }

      case (State.ALIVE, State.EMPTY) => {

        color = nextColor
        seedID = nextSeedID

      }

      case (State.EMPTY, _) => {

        color = Color.BLACK
        seedID = 0

      }

      case _ =>

    }
      super.update()

  }

  def swap(): Unit = currentState match {

    case State.ALIVE => {

      nextState = State.EMPTY
      nextColor = Color.BLACK
      nextSeedID = 0

    }

    case _ => {

      val random = new Random
      nextState = State.ALIVE
      nextColor = new Color(random.nextFloat, random.nextFloat, random.nextFloat, 1)

      nextSeedID = {
        NaiveSeedsGrowthBoard.newID += 1
        NaiveSeedsGrowthBoard.newID
      }

      NaiveSeedsGrowthCell.getSeedList.put(NaiveSeedsGrowthBoard.newID, nextColor)

    }

      update()

  }

  def setSeedID(seedID: Int): Unit = this.seedID = seedID

  def getNextSeedID: Int = nextSeedID

  def setNextSeedID(nextSeedID: Int): Unit = this.nextSeedID = nextSeedID

}