package pl.edu.agh.multiscalemodelling.engine.logic.enumeration

object State extends Enumeration {
  type State = Value
  val EMPTY, ALIVE, SECONDPHASE, INCLUSION, RECRYSTALLIZED = Value
}
