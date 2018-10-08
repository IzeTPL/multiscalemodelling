package pl.edu.agh.multiscalemodelling.model;

public interface ICell {

    boolean checkNeighbors();

    void update();

}
