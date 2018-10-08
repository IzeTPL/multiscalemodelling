package pl.edu.agh.multiscalemodelling.processsimulation

import pl.edu.agh.multiscalemodelling.engine.Application

class Simulation extends Application {
  override def create(): Unit = {
    super.create()
    setScreen(new SimulationScreen(this))
  }

  override def render(): Unit = super.render()

  override def dispose(): Unit = super.dispose()
}