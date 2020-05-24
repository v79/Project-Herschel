package org.liamjd.game.v3.actors

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import ktx.scene2d.RootWidget
import ktx.scene2d.Scene2dDsl
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class AnimationActor(name: String, val animation: Animation<TextureRegion>, var xScale: Float, var yScale: Float) : Actor() {

	lateinit var currentRegion: TextureRegion
	var time = 0f
	private val frameWidth = animation.getKeyFrame(0f).regionWidth.toFloat()
	private val frameHeight = animation.getKeyFrame(0f).regionHeight.toFloat()
	private val initialXScale: Float
	private val initialYScale: Float
	private var initialX: Float = 0f
	private var initialY: Float = 0f

	init {
		println("Initialising animation $name")
		setName(name)
		setScale(xScale,yScale)
		setBounds(this.x,this.y,frameWidth,frameHeight)
		initialXScale = xScale
		initialYScale = yScale

	}

	fun zoom() {
		val middlePoint=  Vector2(x+width/2, y+height/2)
		xScale = xScale * 1.1f
		yScale = yScale * 1.1f
		moveBy(-initialX*0.9f,-initialY*0.9f)

//		setPosition(initialX-frameWidth,initialY-frameHeight)
	}

	fun resetZoom() {
		xScale = initialXScale
		yScale = initialYScale
		moveBy(initialX*1.1f,initialY*1.1f)
//		setPosition(initialX,initialY)
	}

	//... creating animation etc...
	override fun act(delta: Float) {
		time += delta
		currentRegion = animation.getKeyFrame(time, true) as TextureRegion
	}

	override fun draw(batch: Batch, parentAlpha: Float) {
//		super.draw(batch, parentAlpha)

		batch.draw(currentRegion, x, y, currentRegion.regionWidth * xScale, currentRegion.regionHeight * yScale)
	}

}


@Scene2dDsl
@OptIn(ExperimentalContracts::class)
fun RootWidget.animation(name: String, animation: Animation<TextureRegion>,
						 xScale: Float = 1f,
						 yScale: Float = 1f,
						 init: AnimationActor.() -> Unit = {}) : AnimationActor{
	contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
	return storeActor(AnimationActor(name,animation,xScale, yScale)).apply(init)
}

inline fun Actor.onEnter(crossinline listener: () -> Unit): InputListener {
	val enterListener = object : InputListener() {
		override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?)= listener()
	}
	this.addListener(enterListener)
	return enterListener
}

inline fun Actor.onExit(crossinline listener: () -> Unit): InputListener {
	val exitListener = object : InputListener() {
		override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) = listener()
	}
	this.addListener(exitListener)
	return exitListener
}
