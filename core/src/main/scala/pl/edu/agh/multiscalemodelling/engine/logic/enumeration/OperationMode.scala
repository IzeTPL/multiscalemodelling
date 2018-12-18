package pl.edu.agh.multiscalemodelling.engine.logic.enumeration

object OperationMode extends Enumeration {
  type OperationMode = Value
  val SIMPLE_GROWTH, PROBABILITY_RULE, SIMPLE_MCS, RECRYSTALLIZATION_MCS = Value
}
