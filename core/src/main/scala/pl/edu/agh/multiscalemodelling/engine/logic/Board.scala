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
  //neighborhoods.add(new MooreNeighbourHood)

  def seed(): Unit = {
    val random = new Random
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      randomize(cell, random)
      cell.update()
    }
  }

  //TODO amount of nucleons and not repetitive places
  //WTF EVEN IS THIS SHIT and who did that???
  def randomize(cell: Cell, random: Random): Unit = if (random.nextInt(((size.x * size.y) * 0.01f).toInt) == 1) cell.nextState = State.ALIVE

  def clear(): Unit = {

    import scala.collection.JavaConversions._
    for (cell <- cells) {
      cell.nextState = State.EMPTY
      cell.update()
    }

  }

  def draw(progress: Boolean, borders: Boolean): Texture = {
    //val width = Gdx.graphics.getHeight / size.x
    //val height = Gdx.graphics.getHeight / size.y
    val board = new Pixmap(size.x, size.y, Pixmap.Format.RGBA8888)
    import scala.collection.JavaConversions._
    for (cell <- cells) {
      var sameID = 0
      import scala.collection.JavaConversions._
      for (neighbor <- cell.neighbors.head) {
        if (Objects.equals(cell.color, neighbor.color)) {
          sameID += 1
          sameID - 1
        }
      }
      if (progress) if (cell.currentState eq State.EMPTY) board.setColor(Color.BLACK)
      else if (cell.currentState eq State.ALIVE) board.setColor(Color.WHITE)
      else board.setColor(Color.BLUE)
      else if (sameID != cell.neighbors.size && sameID > 0 && borders) board.setColor(Color.BLACK)
      else board.setColor(cell.color)
      board.drawPixel(cell.position.x, cell.position.y)
    }
    val texture = new Texture(board)
    //board.dispose()
    texture
  }

  def save(): Unit = {

    val texture = draw(progress = false, borders = false)

    import javax.swing.JFileChooser
    import javax.swing.JFrame

        val chooser = new JFileChooser
        val f = new JFrame
        f.setVisible(true)
        f.toFront()
        f.setVisible(false)
        val res = chooser.showSaveDialog(f)
        f.dispose()
        if (res == JFileChooser.APPROVE_OPTION) {
          PixmapIO.writePNG(new FileHandle(chooser.getSelectedFile.getName + ".png"), texture.getTextureData.consumePixmap())
        }

  }

  def load(): Unit = {

    var path: String = "aaa.png"

    import javax.swing.JFileChooser
    import javax.swing.JFrame

        val chooser = new JFileChooser
        val f = new JFrame
        f.setVisible(true)
        f.toFront()
        f.setVisible(false)
        val res = chooser.showOpenDialog(f)
        f.dispose()
        if (res == JFileChooser.APPROVE_OPTION) {

          path = chooser.getSelectedFile.getName

        }

    val texture = new Texture(Gdx.files.absolute(path)).getTextureData
    texture.prepare()
    val pixels = texture.consumePixmap

    var i = 0

    while(i < pixels.getWidth) {

      var j = 0

      while(j < pixels.getHeight) {


        cells.get(i*size.x+j).nextColor = new Color(pixels.getPixel(i, j))
        cells.get(i*size.x+j).nextState = State.ALIVE
        cells.get(i*size.x+j).update()

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
      cell.neighbors = List(
        MooreNeighbourHood.findNeighbors(cells, cell, boundaryCondition, size),
        NearestMooreNeighbourhood.findNeighbors(cells, cell, boundaryCondition, size),
        FurtherMooreNeighbourhood.findNeighbors(cells, cell, boundaryCondition, size)
      )
    }
  }

  def getGreaterDimesion: Int = {
    if (size.x > size.y) return size.x
    size.y
  }

  def addInclusions(inclusionType: Int, inclusionAmount: Int, inclusionSize: Int, started: Boolean): Unit = {

    inclusionType match {

      case 0 => circleInclusions(inclusionAmount, inclusionSize, started)
      case 1 => squareInclusions(inclusionAmount, inclusionSize, started)

    }

  }

    def squareInclusions(inclusionAmount: Int, inclusionSize: Int, started: Boolean): Unit = {

      val random = new Random
      var loop = inclusionAmount

      while(loop > 0) {

        val xpos = random.nextInt(size.x)
        val ypos = random.nextInt(size.y)

        var sameID = 0
        import scala.collection.JavaConversions._
        for (neighbor <- cells.get(xpos * size.x + ypos).neighbors.head) {
          if (Objects.equals(cells.get(xpos * size.x + ypos).color, neighbor.color)) {
            sameID += 1
            sameID - 1
          }
        }

        if (sameID != cells.get(xpos * size.x + ypos).neighbors.size && sameID > 0 || !started) {

          var x = xpos - inclusionSize + 1

          while (x < xpos + inclusionSize) {

            var y = ypos - inclusionSize + 1

            while (y < ypos + inclusionSize) {

              val position = new Point(x, y)

              if (position.x < 0) position.x_$eq(size.x - 1)
              if (position.y < 0) position.y_$eq(size.y - 1)
              if (position.x >= size.x) position.x_$eq(0)
              if (position.y >= size.y) position.y_$eq(0)

              cells.get(position.x * size.x + position.y).nextState = State.INCLUSION
              cells.get(position.x * size.x + position.y).update()


              y += 1

            }

            x += 1

          }

          loop -= 1

        }

      }


    }

    def circleInclusions(inclusionAmount: Int, inclusionSize: Int, started: Boolean): Unit = {

      val random = new Random
      var loop = inclusionAmount

      while(loop > 0) {

        val xpos = random.nextInt(size.x)
        val ypos = random.nextInt(size.y)
        var x = xpos - inclusionSize + 1

        var sameID = 0
        import scala.collection.JavaConversions._
        for (neighbor <- cells.get(xpos * size.x + ypos).neighbors.head) {
          if (Objects.equals(cells.get(xpos * size.x + ypos).color, neighbor.color)) {
            sameID += 1
            sameID - 1
          }
        }

        if (sameID != cells.get(xpos * size.x + ypos).neighbors.size && sameID > 0 || !started) {


          while (x < xpos + inclusionSize) {

            val yspan = (inclusionSize * Math.sin(Math.acos((xpos - x).toDouble / inclusionSize.toDouble))).floor.toInt

            var y = ypos - yspan + 1

            while (y < ypos + yspan) {

              val position = new Point(x, y)

              if (position.x < 0) position.x_$eq(size.x - 1)
              if (position.y < 0) position.y_$eq(size.y - 1)
              if (position.x >= size.x) position.x_$eq(0)
              if (position.y >= size.y) position.y_$eq(0)

              cells.get(position.x * size.x + position.y).nextState = State.INCLUSION
              cells.get(position.x * size.x + position.y).update()


              y += 1

            }

            x += 1

          }

          loop -= 1

        }

      }


    }

}