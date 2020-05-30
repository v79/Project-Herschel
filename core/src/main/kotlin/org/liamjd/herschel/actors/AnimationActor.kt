package org.liamjd.herschel.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor

class AnimationActor(name: String, val animation: Animation<TextureRegion>, val xScale: Float, val yScale: Float) : Actor() {

	lateinit var currentRegion: TextureRegion
	var time = 0f
	private val frameWidth = animation.getKeyFrame(0f).regionWidth.toFloat()
	private val frameHeight = animation.getKeyFrame(0f).regionHeight.toFloat()
	public var hover: Boolean = false
	var clicked = false
	val haloRenderer = ShapeRenderer()

	init {
		setName(name)
		setScale(xScale,yScale)
		setBounds(this.x,this.y,frameWidth,frameHeight)
	}

	fun zoom() {
		scaleBy(0.05f)
		moveBy(-10f, -10f)
	}

	fun resetZoom() {
		setScale(xScale, yScale)
		moveBy(10f, 10f)
	}

	//... creating animation etc...
	override fun act(delta: Float) {
		time += delta
		currentRegion = animation.getKeyFrame(time, true) as TextureRegion
	}

	override fun draw(batch: Batch, parentAlpha: Float) {
		if(hover) {
			batch.end()
			haloRenderer.setProjectionMatrix(stage.camera.combined)
			haloRenderer.begin(ShapeRenderer.ShapeType.Line)
			haloRenderer.setColor(Color.WHITE)
			haloRenderer.circle(x + (width*scaleX / 2f),y + (height*scaleY / 2f),((width * scaleX) / 2f))
			haloRenderer.end()
			batch.begin()
		}

		batch.draw(currentRegion, x, y, currentRegion.regionWidth * scaleX, currentRegion.regionHeight * scaleY)
	}

}




