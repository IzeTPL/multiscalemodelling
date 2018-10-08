package pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition

import pl.edu.agh.multiscalemodelling.engine.logic.Point

class PeriodicBoudaryCondition extends BoundaryCondition {
  override def getPosition(position: Point, size: Point): Point = {
    if (position.x < 0) position.x = size.x - 1
    if (position.y < 0) position.y = size.y - 1
    if (position.x >= size.x) position.x = 0
    if (position.y >= size.y) position.y = 0
    position
  }

  override def skip(position: Point, size: Point) = false
}