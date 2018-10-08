package pl.edu.agh.multiscalemodelling.processsimulation;

import pl.edu.agh.multiscalemodelling.engine.Application;

public class Simulation extends Application {

    @Override
    public void create() {

        super.create();
        setScreen(new SimulationScreen(this));

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

        super.dispose();

    }

}
