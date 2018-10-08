package pl.edu.agh.multiscalemodelling.engine.logic;

import java.util.List;

public class LogicThread extends Thread {

    private List<Cell> cells;

    public LogicThread(List<Cell> cells) {

        super();
        this.cells = cells;

    }

    public void run() {

        for (Cell cell : cells) {
            cell.checkNeighbors();
        }

        for (Cell cell : cells) {
            cell.update();
        }

    }

}
