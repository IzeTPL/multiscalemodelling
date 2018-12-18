package pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth

import java._
import java.util.Objects

import com.badlogic.gdx.graphics.Color
import pl.edu.agh.multiscalemodelling.engine.logic.enumeration.{OperationMode, State}
import pl.edu.agh.multiscalemodelling.engine.logic.enumeration.OperationMode.OperationMode
import pl.edu.agh.multiscalemodelling.engine.logic.{Cell, Logic}

import scala.collection.mutable
import scala.util.Random

object NaiveSeedsGrowthCell {

  var seedList: mutable.HashMap[Int, Color] = new mutable.HashMap[Int, Color]()
  var grainShapeControl: Boolean = false
  var probability: Int = 10
  def getSeedList: mutable.HashMap[Int, Color] = seedList

}

class NaiveSeedsGrowthCell(x: Int, y: Int) extends Cell(x, y) {

  seedID = 0
  nextColor = new Color(Color.BLACK)
  nextSeedID = 0

  override def checkNeighbors(mode: OperationMode): Unit = currentState match {

    case State.EMPTY =>
      mode match {
        case OperationMode.SIMPLE_GROWTH =>
          if(NaiveSeedsGrowthCell.grainShapeControl) {
            if (checkRule1())
              if (checkRule2())
                if (checkRule3())
                  checkRule4()
          } else {
            checkDefaultRule()
          }
      }
    case State.ALIVE => Logic.operationMode match {
      case OperationMode.SIMPLE_MCS => mcsRule()
      case OperationMode.RECRYSTALLIZATION_MCS => recrystallize()
      case _ =>
    }
    case _ =>
  }

  def swap(): Unit = {

    currentState match {

      case State.ALIVE => {

        nextState = State.EMPTY
        nextColor = Color.BLACK
        nextSeedID = 0

      }

      case _ =>

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
      case (State.ALIVE, State.ALIVE) =>
        color = nextColor
        seedID = nextSeedID
      case _ => println(s"Unhandled state exception: $currentState -> $nextState")
    }
    super.update()

  }

  def recrystallize(): Unit = {

    val random = new Random
    var energy = 0
    var newEnergy = 0

    if (neighbors.get(0).get(random.nextInt(neighbors.get(0).size)).currentState == State.RECRYSTALLIZED) {

      import scala.collection.JavaConversions._
        for (cell <- neighbors.head) {
          if(cell.currentState == State.ALIVE) {
            if (this.seedID != cell.seedID) energy += 1
            if (seedID != cell.seedID) newEnergy += 1
          }
        }

      energy += recrystallizationEnergy

      if (newEnergy - energy <= 0) {
        nextSeedID = seedID
        nextState = State.RECRYSTALLIZED
        nextColor = NaiveSeedsGrowthCell.seedList.getOrElse(seedID, Color.BLACK)
      }

    }

  }

  def mcsRule(): Unit = {

    var energy = 0
    var newEnergy = 0
    val random = new Random
    val neighborsID = new Array[Int](neighbors.get(0).size)

    var i = 0
    while ( {
      i < neighbors.get(0).size()
    }) {
      neighborsID(i) = neighbors.get(0).get(i).seedID
        i += 1
        i - 1
    }

    var randomIndex = random.nextInt(neighbors.get(0).size)
    var seedID: Int = 0

    while (neighbors.get(0).get(randomIndex).currentState != State.ALIVE) {
      randomIndex = random.nextInt(neighbors.get(0).size)
    }

    seedID = neighborsID(randomIndex)

    import scala.collection.JavaConversions._
    for (cell <- neighbors.head) {
      if(cell.currentState == State.ALIVE) {
        if (this.seedID != cell.seedID) energy += 1
        if (seedID != cell.seedID) newEnergy += 1
      }
    }

    //println(s"Energy: $energy <----------> newEnergy: $newEnergy")

    if (newEnergy - energy <= 0) {
      nextSeedID = seedID
      nextColor = NaiveSeedsGrowthCell.seedList.getOrElse(seedID, Color.BLACK)
    }

  }

  def toRemove: Boolean = {
    var sameID = 0
    import scala.collection.JavaConversions._
    for (cell <- neighbors.head) {
      if (cell.seedID == seedID) {
        sameID += 1
        sameID - 1
      }
    }

    if(sameID == neighbors.head.size() || currentState == State.SECONDPHASE) true else false

  }

  def checkDefaultRule(): Boolean = {

    import scala.collection.JavaConversions._

    if (findMax(neighbors.head) != -1) {

      nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
      nextState = State.ALIVE
      false

    } else true

  }

  def checkRule1(): Boolean = {

    import scala.collection.JavaConversions._

    val max = findMax(neighbors.head)

    if (max != -1 && max >= 5) {

      nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
      nextState = State.ALIVE
      false

    } else true

  }

  def checkRule2(): Boolean = {

    import scala.collection.JavaConversions._

    val max = findMax(neighbors.tail.head)

    if (max != -1 && max >= 3) {

      nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
      nextState = State.ALIVE
      false

    } else true

  }

  def checkRule3(): Boolean = {

    import scala.collection.JavaConversions._

    val max = findMax(neighbors.tail.tail.head)

    if (max != -1 && max >= 3) {

      nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
      nextState = State.ALIVE
      false

    } else true

  }

  def checkRule4(): Boolean = {

    import scala.collection.JavaConversions._

    val max = findMax(neighbors.head)

    val random = new Random

    if (max != -1 && random.nextInt(101) <= NaiveSeedsGrowthCell.probability) {

      nextColor = NaiveSeedsGrowthCell.getSeedList.getOrElse(nextSeedID, Color.MAGENTA)
      nextState = State.ALIVE
      false

    } else true

  }

  def findMax(neighbors: util.List[Cell]): Int = {

    val neighborSeeds = new mutable.HashMap[Int, Int]

    import scala.collection.JavaConversions._

    for (cell: Cell <- neighbors) {
      if (neighborSeeds.contains(cell.seedID) && cell.currentState == State.ALIVE) {

        neighborSeeds.update(cell.seedID, neighborSeeds.getOrElse(cell.seedID, 0) + 1)

      } else {

        if(cell.currentState == State.ALIVE)
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

    max

  }

}