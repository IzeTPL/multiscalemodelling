package pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition;

import pl.edu.agh.multiscalemodelling.engine.logic.Point;

public abstract class BoundaryCondition {

    public abstract Point getPosition(Point position, Point size);

    public abstract boolean skip(Point position, Point size);

}
