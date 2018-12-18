package pl.edu.agh.multiscalemodelling.engine.logic

import java.util

import com.badlogic.gdx.graphics.Color
import pl.edu.agh.multiscalemodelling.engine.logic.enumeration.OperationMode.OperationMode
import pl.edu.agh.multiscalemodelling.engine.logic.enumeration.State
import pl.edu.agh.multiscalemodelling.model.ICell

class Cell(x: Int, y: Int) extends ICell {

  var currentState: State.Value = State.EMPTY
  var nextState: State.Value = State.EMPTY
  var position: Point = _
  var neighbors: util.List[util.List[Cell]] = _
  var color: Color = _
  var nextColor: Color = _
  var seedID: Integer = _
  var nextSeedID: Integer = _
  var recrystallizationEnergy = 0

  position = new Point(x, y)
  color = new Color(Color.WHITE)

  override def checkNeighbors(mode: OperationMode): Unit = {}
  override def update(): Unit = currentState = nextState


}