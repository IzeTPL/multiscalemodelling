package pl.edu.agh.multiscalemodelling.model

trait ICell {
  def checkNeighbors: Unit

  def update(): Unit
}