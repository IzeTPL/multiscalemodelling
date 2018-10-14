package pl.edu.agh.multiscalemodelling.engine.logic

import java.util
import java.util.{Objects, Random}

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.{Color, Pixmap, PixmapIO, Texture}
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.{BoundaryCondition, FixedBoundaryCondition, PeriodicBoudaryCondition}
import pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood._

abstract class Board() {

  var size: Point = _
  var cells: util.List[Cell] = _
  var boundaryConditions: util.List[BoundaryCondition] = _
  var neighborhoods: util.List[Neighborhood] = _

  cells = new util.ArrayList[Cell]
  boundaryConditions = new util.ArrayList[BoundaryCondition]
  neighborhoods = new util.ArrayList[Neighborhood]
  boundaryConditions.add(new FixedBoundaryCondition)
  boundaryConditions.add(new PeriodicBoudaryCondition)
  neighborhoods.add(new MooreNeighbourHood)
  neighborhoods.add(new VonNewmanNeighbourhood)
  neighborhoods.add(new PentagonalRandomNeighbourhood)
  neighborhoods.add(new HexagonalRandomNeighbourhood)
  neighborhoods.add(new HexagonalLeftNeighbourhood)
  neighborhoods.add(new HexagonalRightNeighbourhood)

  def seed(): Unit = {
    val random = new Random
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      randomize(cell, random)
      cell.update()
    }
  }

  //TODO
  def randomize(cell: Cell, random: Random): Unit = if (random.nextInt(((size.x * size.y) * 0.01f).toInt) == 1) cell.setNextState(State.ALIVE)

  def clear(): Unit = {
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      cell.setNextState(State.EMPTY)
      cell.update()
    }
  }

  def draw(progress: Boolean, borders: Boolean): Texture = {
    val width = Gdx.graphics.getHeight / size.x
    val height = Gdx.graphics.getHeight / size.y
    val board = new Pixmap(size.x, size.y, Pixmap.Format.RGBA8888)
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      var sameID = 0
      import scala.collection.JavaConversions._
      for (neighbor <- cell.getNeighbors) {
        if (Objects.equals(cell.color, neighbor.color)) {
          sameID += 1
          sameID - 1
        }
      }
      if (progress) if (cell.getCurrentState eq State.EMPTY) board.setColor(Color.BLACK)
      else if (cell.getCurrentState eq State.ALIVE) board.setColor(Color.WHITE)
      else board.setColor(Color.BLUE)
      else if (sameID != cell.getNeighbors.size && sameID > 0 && borders) board.setColor(Color.BLACK)
      else board.setColor(cell.getColor)
      board.drawPixel(cell.getPosition.x, cell.getPosition.y)
    }
    val texture = new Texture(board)
    //board.dispose()
    texture
  }

  def save(): Unit = {

    val texture = draw(progress = false, borders = false)
    PixmapIO.writePNG(new FileHandle("exported.png"), texture.getTextureData.consumePixmap())

  }

  def load(): Unit = {

    val pixels = new Texture(Gdx.files.absolute("exported.png")).getTextureData.consumePixmap()

    var i = 0

    while(i < pixels.getWidth) {

      var j = 0

      while(j < pixels.getHeight) {

        val pixel: Int = pixels.getPixel(i, j)
        val color: Color = new Color(pixel)

        j+=1
        j-1

      }

      i+=1
      i-1

    }

  }

  def setNeighbourhood(neighbourhood: Neighborhood, boundaryCondition: BoundaryCondition): Unit = {
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      cell.neighbors_(neighbourhood.findNeighbors(cells, cell, boundaryCondition, size))
    }
  }

  def getGreaterDimesion: Int = {
    if (size.x > size.y) return size.x
    size.y
  }

  def getSize: Point = size

  def getCells: util.List[Cell] = cells

  def setCells(cells: util.List[Cell]): Unit = this.cells = cells

  def getBoundaryConditions: util.List[BoundaryCondition] = boundaryConditions

  def getNeighborhoods: util.List[Neighborhood] = neighborhoods
}