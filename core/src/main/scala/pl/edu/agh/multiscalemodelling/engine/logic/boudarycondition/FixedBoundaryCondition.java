package pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition;

import pl.edu.agh.multiscalemodelling.engine.logic.Point;

public class FixedBoundaryCondition extends BoundaryCondition {

    @Override
    public Point getPosition(Point position, Point size) {
        return position;
    }

    @Override
    public boolean skip(Point position, Point size) {
        if (position.x < 0 || position.y < 0 || position.x >= size.x || position.y >= size.y) return true;
        return false;
    }
}
