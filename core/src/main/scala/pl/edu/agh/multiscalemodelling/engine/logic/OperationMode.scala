package pl.edu.agh.multiscalemodelling.engine.logic

object OperationMode extends Enumeration {
  type OperationMode = Value
  val SIMPLE_GROWTH, PROBABILITY_RULE, SIMPLE_MCS = Value
}