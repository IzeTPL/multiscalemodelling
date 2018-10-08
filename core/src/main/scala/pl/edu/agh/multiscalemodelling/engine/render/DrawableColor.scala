package pl.edu.agh.multiscalemodelling.engine.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object DrawableColor {
  def getColor(color: Color): TextureRegionDrawable = getColor(color, 1)

  def getColor(color: Color, alpha: Float): TextureRegionDrawable = getColor(color.r, color.g, color.b, alpha)

  def getColor(r: Float, g: Float, b: Float, a: Float): TextureRegionDrawable = {
    val pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888)
    pixmap.setColor(r, g, b, a)
    pixmap.fill()
    new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)))
  }
}