package pl.edu.agh.multiscalemodelling.engine.logic

import java.util

import com.badlogic.gdx.graphics.Color
import pl.edu.agh.multiscalemodelling.model.ICell

class Cell(x: Int, y: Int) extends ICell {

  var currentState: State.Value = State.EMPTY
  var nextState: State.Value = State.EMPTY
  var position: Point = _
  var neighbors: util.List[util.List[Cell]] = _
  var color: Color = _
  var nextColor: Color = _
  var seedID: Integer = _

  position = new Point(x, y)
  color = new Color(Color.WHITE)

  override def checkNeighbors: Unit = {}
  override def update(): Unit = currentState = nextState


}