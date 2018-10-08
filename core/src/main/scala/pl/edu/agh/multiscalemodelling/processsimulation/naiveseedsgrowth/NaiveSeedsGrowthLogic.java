package pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth;

import pl.edu.agh.multiscalemodelling.engine.logic.Logic;

public class NaiveSeedsGrowthLogic extends Logic {

    public NaiveSeedsGrowthLogic(int x, int y) {

        super();
        board = new NaiveSeedsGrowthBoard(x, y);

    }

    @Override
    public void click(int x, int y) {
        ((NaiveSeedsGrowthBoard) board).swap(x, y);
    }

}
