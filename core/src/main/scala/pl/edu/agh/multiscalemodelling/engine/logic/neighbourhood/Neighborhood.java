package pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood;

import pl.edu.agh.multiscalemodelling.engine.logic.Cell;
import pl.edu.agh.multiscalemodelling.engine.logic.Point;
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.BoundaryCondition;

import java.util.List;

public abstract class Neighborhood {

    public abstract List<Cell> findNeighbors(List<Cell> cells, Cell currentCell, BoundaryCondition boundaryCondition, Point size);

}
