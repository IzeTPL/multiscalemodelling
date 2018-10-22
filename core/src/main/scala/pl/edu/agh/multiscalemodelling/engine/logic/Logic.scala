package pl.edu.agh.multiscalemodelling.engine.logic

abstract class Logic() {

  var board: Board = _
  var isPaused = false
  var started = false

  isPaused = true
  started = false

  def pause(): Unit = isPaused = !isPaused

  def iterate(): Unit = {
    //val threadsNum = Runtime.getRuntime.availableProcessors * Runtime.getRuntime.availableProcessors
    val threadsNum = 1
    var start = 0
    var end = 0
    val threads = new Array[Thread](threadsNum)
    var i = 0
    while (i < threadsNum) {
      if (board.cells.size % threadsNum == 0) end += board.cells.size / threadsNum
      else end += (board.cells.size / threadsNum) + 1
      val condition = (end - start) * (i + 1)
      if (condition > board.cells.size) end -= (condition - board.cells.size)
      threads(i) = createThread(board, start, end)
      threads(i).setName(Integer.toString(i))
      threads(i).start()
      start = end
      i += 1
      i - 1
    }
    i = 0
    while ( {
      i < threadsNum
    }) {
      try threads(i).join()
      catch {
        case e: InterruptedException =>
      }
      i += 1
      i - 1
    }
  }

  def createThread(board: Board, start: Int, end: Int) = new LogicThread(board.cells.subList(start, end))

  def click(x: Int, y: Int): Unit

  def getBoard: Board = board

  def setBoard(board: Board): Unit = this.board = board
}