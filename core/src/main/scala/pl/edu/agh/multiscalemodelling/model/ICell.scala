package pl.edu.agh.multiscalemodelling.model

import pl.edu.agh.multiscalemodelling.engine.logic.OperationMode.OperationMode

trait ICell {
  def checkNeighbors(mode: OperationMode): Unit

  def update(): Unit
}