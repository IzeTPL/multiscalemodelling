package pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood

import java.util

import pl.edu.agh.multiscalemodelling.engine.logic.{Cell, Point}
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.BoundaryCondition

trait Neighborhood {
  def findNeighbors(cells: util.List[Cell], currentCell: Cell, boundaryCondition: BoundaryCondition, size: Point): util.List[Cell]
}