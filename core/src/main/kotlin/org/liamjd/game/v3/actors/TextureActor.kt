package org.liamjd.game.v3.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class TextureActor(private val texture: Texture) : Actor() {
	override fun draw(batch: Batch, parentAlpha: Float) {
		batch.draw(texture, x, y)
	}
}
