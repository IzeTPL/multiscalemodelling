package pl.edu.agh.multiscalemodelling.engine.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import pl.edu.agh.multiscalemodelling.engine.Application
import pl.edu.agh.multiscalemodelling.engine.logic.Logic

abstract class AbstractScreen(var application: Application) extends Screen {

  var logic: Logic = _
  var timer = .0
  var cellCamera: Camera = _
  var uiCamera: Camera = _
  var cellViewport: Viewport = _
  var uiViewport: Viewport = _
  var stage: Stage = _
  var root: Table = _
  var table: Table = _
  var showProgressbool = false
  var showBordersbool = false

  timer = 0
  cellCamera = new OrthographicCamera
  uiCamera = new OrthographicCamera
  cellViewport = new FitViewport(Gdx.graphics.getHeight, Gdx.graphics.getHeight, cellCamera)
  uiViewport = new FitViewport(Gdx.graphics.getWidth - Gdx.graphics.getHeight, Gdx.graphics.getHeight, uiCamera)
  cellCamera.position.set(cellViewport.getWorldWidth / 2, cellViewport.getWorldHeight / 2, 0)
  uiCamera.position.set(uiViewport.getWorldWidth / 2, uiViewport.getWorldHeight / 2, 0)
  stage = new Stage(uiViewport, this.application.getSpriteBatch)
  root = new Table
  table = new Table
  table.setBackground(DrawableColor.getColor(Color.DARK_GRAY))
  root.setBackground(DrawableColor.getColor(Color.GRAY))
  root.setFillParent(true)
  root.add(table).width(Gdx.graphics.getWidth - Gdx.graphics.getHeight).left.top.expand
  stage.addActor(root)


  override def show(): Unit = {
  }

  override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    uiViewport.setCamera(uiCamera)
    uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth - Gdx.graphics.getHeight, Gdx.graphics.getHeight)
    uiViewport.apply()
    application.getSpriteBatch.setProjectionMatrix(uiCamera.combined)
    stage.act()
    stage.draw()
    cellViewport.setCamera(cellCamera)
    cellViewport.setScreenBounds(Gdx.graphics.getWidth - Gdx.graphics.getHeight, 0, Gdx.graphics.getHeight, Gdx.graphics.getHeight)
    cellViewport.apply()
    application.getSpriteBatch.setProjectionMatrix(cellCamera.combined)
    application.getSpriteBatch.begin()
    val width = Gdx.graphics.getHeight.toFloat / (logic.getBoard.getGreaterDimesion.toFloat / logic.getBoard.getSize.x.toFloat)
    val height = Gdx.graphics.getHeight.toFloat / (logic.getBoard.getGreaterDimesion.toFloat / logic.getBoard.getSize.y.toFloat)
    val texture = logic.getBoard.draw(showProgressbool, showBordersbool)
    application.getSpriteBatch.draw(texture, 0, Gdx.graphics.getHeight - height, width, height)
    application.getSpriteBatch.end()
    texture.dispose()
    Gdx.input.setInputProcessor(stage)
  }

  override def resize(width: Int, height: Int): Unit = {
    cellViewport.setWorldSize(height, height)
    cellViewport.update(width, height, true)
    uiViewport.setWorldSize(width - height, height)
    uiViewport.update(width, height, true)
  }

  override def pause(): Unit = {
  }

  override def resume(): Unit = {
  }

  override def hide(): Unit = {
  }

  override def dispose(): Unit = {
    application.dispose()
    stage.dispose()
  }

  def update(delta: Float): Unit = {
    cellCamera.update()
    uiCamera.update()
  }
}