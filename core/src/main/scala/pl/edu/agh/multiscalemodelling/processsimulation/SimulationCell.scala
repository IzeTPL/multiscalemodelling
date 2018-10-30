package pl.edu.agh.multiscalemodelling.processsimulation

import java.util

import com.badlogic.gdx.graphics.Color
import pl.edu.agh.multiscalemodelling.engine.logic.Cell

object SimulationCell {
  protected var seedList: util.HashMap[Integer, Color] = null
}

class SimulationCell(x: Int, y: Int) extends Cell(x, y) {
/*  var nextSeedID: Integer = null*/
}