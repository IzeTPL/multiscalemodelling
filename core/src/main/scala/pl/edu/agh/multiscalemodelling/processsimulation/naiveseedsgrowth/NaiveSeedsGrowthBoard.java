package pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth;

import com.badlogic.gdx.graphics.Color;
import pl.edu.agh.multiscalemodelling.engine.logic.Board;
import pl.edu.agh.multiscalemodelling.engine.logic.Cell;
import pl.edu.agh.multiscalemodelling.engine.logic.Point;
import pl.edu.agh.multiscalemodelling.engine.logic.State;

import java.util.Random;

public class NaiveSeedsGrowthBoard extends Board {

    protected static int newID = 0;

    public NaiveSeedsGrowthBoard(int x, int y) {

        super();

        size = new Point(x, y);

        for (int i = 0; i < size.x; i++) {

            for (int j = 0; j < size.y; j++) {

                cells.add(new NaiveSeedsGrowthCell(i, j));

            }


        }

    }

    @Override
    public void clear() {


        for (Cell cell : cells) {
            cell.setNextState(State.EMPTY);
            cell.setNextColor(Color.BLACK);
            cell.update();
        }


    }

    @Override
    public void randomize(Cell cell, Random random) {

        super.randomize(cell, random);

        boolean test = ((NaiveSeedsGrowthCell) cell).getSeedID() == 0;

        if (cell.getNextState() == State.ALIVE && test) {
            Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
            while ((color.r + color.g + color.a) < 0.5f) {
                color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
            }
            cell.setNextColor(color);
            cell.setNextState(State.ALIVE);
            ((NaiveSeedsGrowthCell) cell).setNextSeedID(++newID);
            NaiveSeedsGrowthCell.getSeedList().put(newID, color);
        }

    }

    public void swap(int x, int y) {

        ((NaiveSeedsGrowthCell) cells.get(x * size.x + y)).swap();

    }

    public void seed(int distance) {

        Random random = new Random();

        int distanceY = 0;


        for (int i = 0; i < size.x; i++) {

            int distanceX = 0;
            if (distanceY == distance + 1) distanceY = 0;
            for (int j = 0; j < size.y; j++) {

                if (distanceX == distance + 1) distanceX = 0;
                if (distanceX == distance && distanceY == distance) {
                    Cell cell = cells.get(i * size.x + j);
                    cell.setNextState(State.ALIVE);
                    ((NaiveSeedsGrowthCell) cell).setSeedID(++newID);
                    cell.update();
                    if (cell.getCurrentState() == State.ALIVE) {
                        cell.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1));
                    }
                }
                distanceX++;

            }
            distanceY++;

        }

    }

    public void radiusSeed(int radius) {

        Random random = new Random();
        Point position;

        for (Cell cell : cells) {

            boolean inside = false;

            for (int x = cell.getPosition().x - radius; x < cell.getPosition().x + radius; x++) {

                int yspan = (int) Math.round(radius * Math.sin(Math.acos((cell.getPosition().x - x) / radius)));

                for (int y = cell.getPosition().y - yspan; y < cell.getPosition().y + yspan; y++) {

                    position = new Point(x, y);
                    if (position.x < 0) position.x = size.x - 1;
                    if (position.y < 0) position.y = size.y - 1;
                    if (position.x >= size.x) position.x = 0;
                    if (position.y >= size.y) position.y = 0;
                    if (cells.get(position.x * size.x + position.y).getCurrentState() == State.ALIVE) inside = true;
                    if (inside) break;

                }

                if (inside) break;

            }

            if (!inside) {
                randomize(cell, random);
            }

        }

    }

}
