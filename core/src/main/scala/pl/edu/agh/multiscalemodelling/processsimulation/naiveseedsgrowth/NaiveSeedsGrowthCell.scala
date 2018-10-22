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

  var nextSeedID: Integer = _

  override def checkNeighbors: Boolean = currentState match {

    case State.EMPTY => {

      checkRule1()
      checkRule2()
      checkRule3()
      checkRule4()

      true

    }

    case _ => false

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

      case (State.EMPTY, State.EMPTY) => {

        color = Color.WHITE
        seedID = 0

      }

      case (State.ALIVE, State.INCLUSION) => {

        color = Color.BLACK
        seedID = 0

      }

      case (State.EMPTY, State.INCLUSION) => {

        color = Color.BLACK
        seedID = 0

      }

      case _ =>

    }
    super.update()

  }

  def checkRule1(): Unit = {

    val neighborSeeds = new mutable.HashMap[Int, Int]

    import scala.collection.JavaConversions._

    for (cell: Cell <- neighbors.head) {
      if (neighborSeeds.contains(cell.seedID)) {

        neighborSeeds.update(cell.seedID, neighborSeeds.getOrElse(cell.seedID, 0) + 1)

      } else {

        neighborSeeds.put(cell.seedID, 1)

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

    if (max != -1 && max >= 5) {

      nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
      nextState = State.ALIVE

    }

  }

  def checkRule2(): Unit = {

    val neighborSeeds = new mutable.HashMap[Int, Int]

    import scala.collection.JavaConversions._

    for (cell: Cell <- neighbors.tail.head) {
      if (neighborSeeds.contains(cell.seedID)) {

        neighborSeeds.update(cell.seedID, neighborSeeds.getOrElse(cell.seedID, 0) + 1)

      } else {

        neighborSeeds.put(cell.seedID, 1)

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

    if (max != -1 && max >= 3) {

      nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
      nextState = State.ALIVE

    }

  }

  def checkRule3(): Unit = {

    val neighborSeeds = new mutable.HashMap[Int, Int]

    import scala.collection.JavaConversions._

    for (cell: Cell <- neighbors.tail.tail.head) {
      if (neighborSeeds.contains(cell.seedID)) {

        neighborSeeds.update(cell.seedID, neighborSeeds.getOrElse(cell.seedID, 0) + 1)

      } else {

        neighborSeeds.put(cell.seedID, 1)

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

    if (max != -1 && max >= 3) {

      nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
      nextState = State.ALIVE

    }

  }

  def checkRule4(): Unit = {

    val neighborSeeds = new mutable.HashMap[Int, Int]
    val random = new Random

    import scala.collection.JavaConversions._

    for (cell: Cell <- neighbors.tail.tail.head) {
      if (neighborSeeds.contains(cell.seedID)) {

        neighborSeeds.update(cell.seedID, neighborSeeds.getOrElse(cell.seedID, 0) + 1)

      } else {

        neighborSeeds.put(cell.seedID, 1)

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

    if (max != -1 && random.nextInt(101) <= 10) {

      nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
      nextState = State.ALIVE

    }

  }

}