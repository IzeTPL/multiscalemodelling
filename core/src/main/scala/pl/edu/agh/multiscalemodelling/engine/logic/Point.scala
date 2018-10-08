package pl.edu.agh.multiscalemodelling.engine.logic

class Point {
  var x = 0
  var y = 0
  var z = 0

  def this(x: Int) {
    this()
    this.x = x
    this.y = 1
    this.z = 1
  }

  def this(x: Int, y: Int) {
    this()
    this.x = x
    this.y = y
    this.z = 1
  }

  def this(x: Int, y: Int, z: Int) {
    this()
    this.x = x
    this.y = y
    this.z = z
  }
}