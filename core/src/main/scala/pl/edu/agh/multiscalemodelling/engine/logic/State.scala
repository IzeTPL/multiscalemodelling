package pl.edu.agh.multiscalemodelling.engine.logic

object State extends Enumeration {
  type State = Value
  val EMPTY, ALIVE, RECRYSTALLIZED, INCLUSION = Value
}