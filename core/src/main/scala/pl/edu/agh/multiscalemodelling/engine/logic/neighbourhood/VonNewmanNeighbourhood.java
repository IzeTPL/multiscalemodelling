package pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood;

import pl.edu.agh.multiscalemodelling.engine.logic.Cell;
import pl.edu.agh.multiscalemodelling.engine.logic.Point;
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.BoundaryCondition;

import java.util.ArrayList;
import java.util.List;

public class VonNewmanNeighbourhood extends Neighborhood {

    @Override
    public List<Cell> findNeighbors(List<Cell> cells, Cell currentCell, BoundaryCondition boundaryCondition, Point size) {
        List<Cell> neighbors = new ArrayList<>();

        for (int i = currentCell.getPosition().x - 1; i <= currentCell.getPosition().x + 1; i++) {
            for (int j = currentCell.getPosition().y - 1; j <= currentCell.getPosition().y + 1; j++) {

                Point position = new Point(i, j);
                position = boundaryCondition.getPosition(position, size);

                if ((currentCell.getPosition().x != i && currentCell.getPosition().y != j) || (currentCell.getPosition().x == position.x && currentCell.getPosition().y == position.y) || boundaryCondition.skip(position, size)) {
                    continue;
                }

                neighbors.add(cells.get(position.x * size.x + position.y));

            }
        }

        return neighbors;
    }

}
