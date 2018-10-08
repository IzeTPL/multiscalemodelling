package pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition

import pl.edu.agh.multiscalemodelling.engine.logic.Point

class FixedBoundaryCondition extends BoundaryCondition {
  override def getPosition(position: Point, size: Point): Point = position

  override def skip(position: Point, size: Point): Boolean = {
    if (position.x < 0 || position.y < 0 || position.x >= size.x || position.y >= size.y) return true
    false
  }
}