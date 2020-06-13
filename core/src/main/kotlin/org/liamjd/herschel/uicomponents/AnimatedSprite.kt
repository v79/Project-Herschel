package org.liamjd.herschel.uicomponents

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import org.liamjd.herschel.models.solarsystems.Planet

open class AnimatedSprite(atlasPath: String, frameCount: Float = 15f) : Sprite() {

	val animation: Animation<TextureRegion>
	val spriteWidth: Float
	val spriteHeight: Float

	var rectangle: Rectangle = Rectangle()

	init {
		val textureAtlas: TextureAtlas = TextureAtlas(Gdx.files.internal(atlasPath))

		animation = Animation<TextureRegion>(1f/frameCount,textureAtlas.regions,Animation.PlayMode.LOOP)
		spriteWidth = textureAtlas.regions.first().regionWidth.toFloat()
		spriteHeight = textureAtlas.regions.first().regionHeight.toFloat()

	}

	fun setRectangle(x: Float, y: Float, w: Float, h: Float) {
		rectangle.set(x,y,w * spriteWidth,h * spriteHeight)
	}

}

class PlanetAnimation(val planet: Planet, val xPos: Float, val drawScale: Float) : AnimatedSprite("planets/${planet.name.toLowerCase()}.atlas") {



}
