package pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood

import java.util

import pl.edu.agh.multiscalemodelling.engine.logic.{Cell, Point}
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.BoundaryCondition

class MooreNeighbourHood extends Neighborhood {
  override def findNeighbors(cells: util.List[Cell], currentCell: Cell, boundaryCondition: BoundaryCondition, size: Point): util.List[Cell] = {
    val neighbors = new util.ArrayList[Cell]
    var i = currentCell.getPosition.x - 1
    while ( {
      i <= currentCell.getPosition.x + 1
    }) {
      var j = currentCell.getPosition.y - 1
      while ( {
        j <= currentCell.getPosition.y + 1
      }) {
        var position = new Point(i, j)
        position = boundaryCondition.getPosition(position, size)
        if (!((currentCell.getPosition.x == position.x && currentCell.getPosition.y == position.y) || boundaryCondition.skip(position, size))) neighbors.add(cells.get(position.x * size.x + position.y))
        j += 1
        j - 1
      }
      i += 1
      i - 1
    }
    neighbors
  }
}