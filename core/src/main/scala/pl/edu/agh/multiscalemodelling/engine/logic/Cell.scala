package pl.edu.agh.multiscalemodelling.engine.logic

import com.badlogic.gdx.graphics.Color
import pl.edu.agh.multiscalemodelling.model.ICell
import java.util.List

class Cell(x: Int, y: Int) extends ICell {

  var currentState: State = _
  var nextState: State = _
  var position: Point = _
  var neighbors: List[Cell] = _
  var color: Color = _
  var nextColor: Color = _
  var seedID: Integer = _

  position = new Point(x, y)
  color = new Color(Color.BLACK)
  currentState = State.EMPTY
  nextState = State.EMPTY

  def getSeedID: Integer = seedID

  def setSeedID(seedID: Integer): Unit = this.seedID = seedID

  override def checkNeighbors = false

  override def update(): Unit = currentState = nextState

  def getCurrentState: State = currentState

  def setCurrentState(currentState: State): Unit = this.currentState = currentState

  def getNextState: State = nextState

  def setNextState(nextState: State): Unit = this.nextState = nextState

  def getPosition: Point = position

  def setPosition(position: Point): Unit = this.position = position

  def getColor: Color = color

  def setColor(color: Color): Unit = this.color = color

  def getNextColor: Color = nextColor

  def setNextColor(nextColor: Color): Unit = this.nextColor = nextColor

  def getNeighbors: List[Cell] = neighbors

  def neighbors_(value: List[Cell]): Unit = neighbors = value

}