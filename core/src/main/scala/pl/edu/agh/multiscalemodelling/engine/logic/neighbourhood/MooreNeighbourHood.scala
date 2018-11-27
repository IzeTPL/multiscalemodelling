package pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood

import java.util

import pl.edu.agh.multiscalemodelling.engine.logic.{Cell, Point}
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.BoundaryCondition
import pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth.NaiveSeedsGrowthCell

object MooreNeighbourHood extends Neighborhood {
  override def findNeighbors(cells: util.List[NaiveSeedsGrowthCell], currentCell: Cell, boundaryCondition: BoundaryCondition, size: Point): util.List[Cell] = {
    val neighbors = new util.ArrayList[Cell]
    var i = currentCell.position.x - 1
    while ( {
      i <= currentCell.position.x + 1
    }) {
      var j = currentCell.position.y - 1
      while ( {
        j <= currentCell.position.y + 1
      }) {
        var position = new Point(i, j)
        position = boundaryCondition.getPosition(position, size)
        if (!((currentCell.position.x == position.x && currentCell.position.y == position.y) || boundaryCondition.skip(position, size))) neighbors.add(cells.get(position.x * size.x + position.y))
        j += 1
        j - 1
      }
      i += 1
      i - 1
    }
    neighbors
  }
}