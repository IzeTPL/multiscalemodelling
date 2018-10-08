package pl.edu.agh.multiscalemodelling

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import pl.edu.agh.multiscalemodelling.processsimulation.Simulation

object Main extends App {

  val config = new LwjglApplicationConfiguration

  config.title = "multiscale-modelling"
  config.width = 1200
  config.height = 700
  config.vSyncEnabled = false
  config.resizable = true

  new LwjglApplication(new Simulation, config)

}
