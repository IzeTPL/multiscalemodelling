package pl.edu.agh.multiscalemodelling.engine.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.BoundaryCondition;
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.FixedBoundaryCondition;
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.PeriodicBoudaryCondition;
import pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class Board {

    protected Point size;
    protected List<Cell> cells;
    protected List<BoundaryCondition> boundaryConditions;
    protected List<Neighborhood> neighborhoods;

    public Board() {

        cells = new ArrayList<>();
        boundaryConditions = new ArrayList<>();
        neighborhoods = new ArrayList<>();

        boundaryConditions.add(new FixedBoundaryCondition());
        boundaryConditions.add(new PeriodicBoudaryCondition());

        neighborhoods.add(new MooreNeighbourHood());
        neighborhoods.add(new VonNewmanNeighbourhood());
        neighborhoods.add(new PentagonalRandomNeighbourhood());
        neighborhoods.add(new HexagonalRandomNeighbourhood());
        neighborhoods.add(new HexagonalLeftNeighbourhood());
        neighborhoods.add(new HexagonalRightNeighbourhood());

    }

    public void seed() {

        Random random = new Random();

        for (Cell cell : cells) {

            randomize(cell, random);
            cell.update();

        }


    }

    public void clear() {


        for (Cell cell : cells) {
            cell.setNextState(State.EMPTY);
            cell.update();
        }

    }

    public Texture draw(boolean progress, boolean borders) {

        float width = Gdx.graphics.getHeight() / size.x;
        float height = Gdx.graphics.getHeight() / size.y;

        Pixmap board = new Pixmap(size.x, size.y, Pixmap.Format.RGBA8888);


        for (Cell cell : cells) {

            int sameID = 0;

            for (Cell neighbor : cell.getNeighbors()) {

                if (Objects.equals(cell.color(), neighbor.color())) sameID++;

            }

            if (progress) {
                if (cell.getCurrentState() == State.EMPTY) board.setColor(Color.BLACK);
                else if (cell.getCurrentState() == State.ALIVE) board.setColor(Color.WHITE);
                else board.setColor(Color.BLUE);
            } else if (sameID != cell.getNeighbors().size() && sameID > 0 && borders) {
                board.setColor(Color.BLACK);
            } else {
                board.setColor(cell.getColor());
            }
            board.drawPixel(cell.getPosition().x, cell.getPosition().y);

        }


        Texture texture = new Texture(board);

        board.dispose();

        return texture;

    }

    public void randomize(Cell cell, Random random) {

        if (random.nextInt((int) ((size.x * size.y) * 0.01f)) == 1) {
            cell.setNextState(State.ALIVE);
        }

    }

    public void setNeighbourhood(Neighborhood neighbourhood, BoundaryCondition boundaryCondition) {


        for (Cell cell : cells) {

            cell.neighbors_(neighbourhood.findNeighbors(cells, cell, boundaryCondition, size));

        }

    }

    public int getGreaterDimesion() {

        if (size.x > size.y) return size.x;
        return size.y;

    }

    public Point getSize() {
        return size;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public List<BoundaryCondition> getBoundaryConditions() {
        return boundaryConditions;
    }

    public List<Neighborhood> getNeighborhoods() {
        return neighborhoods;
    }


}
