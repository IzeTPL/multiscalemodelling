package pl.edu.agh.multiscalemodelling.engine.logic

import java.util
import java.util.{Objects, Random}

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.{Color, Pixmap, PixmapIO, Texture}
import pl.edu.agh.multiscalemodelling.engine.logic.boudarycondition.{BoundaryCondition, FixedBoundaryCondition, PeriodicBoudaryCondition}
import pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood._
import pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth.{NaiveSeedsGrowthBoard, NaiveSeedsGrowthCell}

import scala.collection.mutable
import scala.io.Source

abstract class Board() {

  var size: Point = _
  var cells: util.List[Cell] = _
  var boundaryConditions: util.List[BoundaryCondition] = _
  var neighborhoods: util.List[Neighborhood] = _
  var selectedGrains: mutable.HashSet[Int] = new mutable.HashSet[Int]()

  cells = new util.ArrayList[Cell]
  boundaryConditions = new util.ArrayList[BoundaryCondition]
  neighborhoods = new util.ArrayList[Neighborhood]
  boundaryConditions.add(new FixedBoundaryCondition)
  boundaryConditions.add(new PeriodicBoudaryCondition)

  def seed(amount: Int): Unit = {
    val random = new Random
    var i = 0
    while (i < amount) {
      val cell = cells.get(random.nextInt(size.x * size.y))
      randomize(cell, random)
      if(cell.nextState == State.ALIVE ){
        i+=1
        i - 1
      }
      cell.update()
    }
  }

  def randomize(cell: Cell, random: Random): Unit = if (cell.currentState == State.EMPTY) cell.nextState = State.ALIVE

  def clear(): Unit = {

    import scala.collection.JavaConversions._
    for (cell <- cells) {
      cell.nextState = State.EMPTY
      cell.update()
    }

  }

  def draw(progress: Boolean, borders: Boolean, boundaryType: Int): Texture = {

    val board = new Pixmap(size.x, size.y, Pixmap.Format.RGBA8888)
    import scala.collection.JavaConversions._
    for (cell <- cells) {

      board.setColor(cell.color)
      board.drawPixel(cell.position.x, cell.position.y)

    }
    val texture = new Texture(board)
    //board.dispose()
    texture
  }

  def save(outputType: Int): Unit = {

    import javax.swing.JFileChooser
    import javax.swing.JFrame

    val chooser = new JFileChooser
    val f = new JFrame
    f.setVisible(true)
    f.toFront()
    f.setVisible(false)
    val res = chooser.showSaveDialog(f)
    f.dispose()

    outputType match {

      case 0 => {
        if (res == JFileChooser.APPROVE_OPTION) {
          val pw = new java.io.PrintWriter(chooser.getSelectedFile.getName + ".txt")
          import scala.collection.JavaConversions._
          pw.println(List(size.x, size.y).mkString(","))
          for (cell <- cells) {
            pw.println(List(cell.position.x, cell.position.y, cell.seedID).mkString(","))
          }
          pw.close()
        }
      }

      case 1 => {
        val texture = draw(progress = false, borders = false, 0)
        if (res == JFileChooser.APPROVE_OPTION) {
          PixmapIO.writePNG(new FileHandle(chooser.getSelectedFile.getName + ".png"), texture.getTextureData.consumePixmap())
        }
      }

    }

  }

  def load(outputType: Int): Unit = {

    var path = "aaa"

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

    outputType match {

      case 0 => {
        if (res == JFileChooser.APPROVE_OPTION) {
          val myVarsFromFile = Source.fromFile(path).getLines.toList

          if (myVarsFromFile.nonEmpty) {
            cells = new util.ArrayList[Cell]()
            NaiveSeedsGrowthCell.seedList = new mutable.HashMap[Int, Color]()
            val boardData = myVarsFromFile.head.split(",")
            size.x = boardData(0).toInt
            size.y = boardData(1).toInt
            myVarsFromFile.tail.foreach( line => {
              val cellData = line.split(",")
              val cell = new NaiveSeedsGrowthCell(cellData(0).toInt, cellData(1).toInt)
              cell.seedID = cellData(2).toInt
              cell.nextState = State.ALIVE
              cell.color = NaiveSeedsGrowthCell.seedList.getOrElse(cell.seedID, {
                val color: Color = new Color(new Random().nextInt(Int.MaxValue))
                color.a = 1
                NaiveSeedsGrowthCell.seedList.put(cell.seedID, color)
                color
              })
              cell.nextColor = cell.color
              cell.update()
              cells.add(cell)
            })

            import scala.collection.JavaConversions._
            for(cell <- cells) {

              cell.neighbors = List(MooreNeighbourHood.findNeighbors(cells, cell, new PeriodicBoudaryCondition, size))

            }

          }
        }
      }

      case 1 => {
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

        if (sameID != cells.get(xpos * size.x + ypos).neighbors.head.size && sameID > 0 || !started) {

          var x = xpos - inclusionSize + 1

          while (x < xpos + inclusionSize) {

            var y = ypos - inclusionSize + 1

            while (y < ypos + inclusionSize) {

              val position = new Point(x, y)

              if (position.x < 0) position.x = size.x - 1
              if (position.y < 0) position.y = size.y - 1
              if (position.x >= size.x) position.x = 0
              if (position.y >= size.y) position.y = 0

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
          if (cells.get(xpos * size.x + ypos).color.toIntBits == neighbor.color.toIntBits) {
            sameID += 1
            sameID - 1
          }
        }

        if (sameID != cells.get(xpos * size.x + ypos).neighbors.head.size && sameID > 0 || !started) {


          while (x < xpos + inclusionSize) {

            val yspan = (inclusionSize * Math.sin(Math.acos((xpos - x).toDouble / inclusionSize.toDouble))).floor.toInt

            var y = ypos - yspan + 1

            while (y < ypos + yspan) {

              val position = new Point(x, y)

              if (position.x < 0) position.x = size.x - 1
              if (position.y < 0) position.y = size.y - 1
              if (position.x >= size.x) position.x = 0
              if (position.y >= size.y) position.y = 0

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

  def addBorders(borderType: Int): Unit = {

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

      var skip = false
      if (borderType == 1 && !selectedGrains.contains(cell)) skip = true

      if (sameID != cell.neighbors.head.size && sameID > 0 && !skip) {
        cell.nextState = State.INCLUSION
        cell.nextColor = Color.BLACK
      }

    }

  }

  def selectGrain(x: Int, y: Int): Unit = {

    val cell: Cell = cells.get(x * size.x + y)
    if (selectedGrains contains cell.seedID) selectedGrains -= cell.seedID else selectedGrains += cell.seedID

  }

  def secondStep(stepType: Int): Unit = {

    if(stepType == 1) NaiveSeedsGrowthBoard.newID+=1
    import scala.collection.JavaConversions._
    for(cell <- cells) {

      if(!selectedGrains.contains(cell.seedID)) {

        cell.nextSeedID = 0
        cell.nextState = State.EMPTY
        cell.nextColor = Color.WHITE

      } else {

        stepType match {

          case 0 => cell.nextState = State.SECONDPHASE
          case 1 => {

            cell.nextState = State.SECONDPHASE
            cell.color = Color.MAGENTA
            cell.nextColor = Color.MAGENTA
            cell.nextSeedID = NaiveSeedsGrowthBoard.newID

          }

        }

      }

      cell.update()

    }

  }

}