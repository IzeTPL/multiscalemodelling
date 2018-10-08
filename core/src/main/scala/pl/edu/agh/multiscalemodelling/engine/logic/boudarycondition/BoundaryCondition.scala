package pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition

import pl.edu.agh.multiscalemodelling.engine.logic.Point

abstract class BoundaryCondition {
  def getPosition(position: Point, size: Point): Point

  def skip(position: Point, size: Point): Boolean
}