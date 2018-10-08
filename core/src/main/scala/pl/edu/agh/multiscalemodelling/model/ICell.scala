package pl.edu.agh.multiscalemodelling.model

trait ICell {
  def checkNeighbors: Boolean

  def update(): Unit
}