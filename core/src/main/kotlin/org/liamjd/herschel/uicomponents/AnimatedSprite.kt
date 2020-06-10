package org.liamjd.herschel.uicomponents

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

open class AnimatedSprite(atlasPath: String, val frameCount: Float = 30f) : Sprite() {

	val animation: Animation<TextureRegion>
	val w: Float
	val h: Float

	init {
		val textureAtlas: TextureAtlas = TextureAtlas(Gdx.files.internal(atlasPath))

		animation = Animation<TextureRegion>(1f/frameCount,textureAtlas.regions,Animation.PlayMode.LOOP)
		w = textureAtlas.regions.first().regionWidth.toFloat()
		h = textureAtlas.regions.first().regionHeight.toFloat()
	}
}

class PlanetAnimation(val planetName: String, val xPos: Float, val drawScale: Float) : AnimatedSprite("planets/$planetName.atlas") {

}
