package pl.edu.agh.multiscalemodelling.processsimulation;

import com.badlogic.gdx.graphics.Color;
import pl.edu.agh.multiscalemodelling.engine.logic.Cell;

import java.util.HashMap;

public class SimulationCell extends Cell {

    protected static HashMap<Integer, Color> seedList;
    protected Integer seedID;
    protected Integer nextSeedID;

    public SimulationCell(int x, int y) {
        super(x, y);
    }

}
