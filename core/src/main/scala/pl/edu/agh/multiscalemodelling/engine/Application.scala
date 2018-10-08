package pl.edu.agh.multiscalemodelling.engine

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import pl.edu.agh.multiscalemodelling.engine.logic.Board

abstract class Application extends Game {
  var spriteBatch: SpriteBatch = _
  var board: Board = _

  override def create(): Unit = spriteBatch = new SpriteBatch

  override def render(): Unit = super.render()

  override def dispose(): Unit = {
    super.dispose()
    spriteBatch.dispose()
  }

  def getSpriteBatch: SpriteBatch = spriteBatch

  def setSpriteBatch(spriteBatch: SpriteBatch): Unit = this.spriteBatch = spriteBatch
}