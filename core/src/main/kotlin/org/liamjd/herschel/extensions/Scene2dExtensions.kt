package org.liamjd.herschel.extensions

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.RootWidget
import ktx.scene2d.Scene2dDsl
import org.liamjd.herschel.actors.AnimationActor
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun Label.onExit(crossinline listener: () -> Unit): InputListener {
	val eventListener = object : InputListener() {

		override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
			super.exit(event, x, y, pointer, toActor)
			return listener()
		}
	}
	this.addListener(eventListener)
	return eventListener
}

/**
 * Additional actions on all actors, so Labels have onEnter actions
 */
inline fun Actor.onEnter(crossinline listener: () -> Unit): InputListener {
	val enterListener = object : InputListener() {
		override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
			return listener()
		}

		override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
			return listener()
		}
	}
	this.addListener(enterListener)
	return enterListener
}

/**
 * Add an onHover action to AnimationActor. To do this is it ignores click events during the action.
 * [onEnterFunction] function to execute when the hover starts
 * [onExitFunction] function to execute when the hover ends
 * [listener] function to execute during the over state
 */
inline fun AnimationActor.onHover(crossinline onEnterFunction: () -> Unit, crossinline onExitFunction: () -> Unit, crossinline listener: () -> Unit): InputListener {
	val eventListener = object : InputListener() {
		override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
			// if pointer = 1 then we have a click event, which I am not interested in
			if (pointer != 0) {
				hover = true
				onEnterFunction()
				super.enter(event, x, y, pointer, fromActor)
				return listener()
			}
			return
		}

		override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
			// if pointer = 1 then we have a click event, which I am not interested in
			if (pointer != 0) {
				hover = false
				onExitFunction()
				super.exit(event, x, y, pointer, toActor)
				return listener()
			}
			return
		}
	}
	this.addListener(eventListener)
	return eventListener
}

/**
 * Construct and add an AnimationActor to the stage
 * [name] simple name for the actor
 * [animation] animated texture region
 * [xScale] scale the source image in the X direction
 * [yScale] scale the source image in the Y direction
 */
@Scene2dDsl
@OptIn(ExperimentalContracts::class)
fun RootWidget.animation(name: String, animation: Animation<TextureRegion>,
						 xScale: Float = 1f,
						 yScale: Float = 1f,
						 init: AnimationActor.() -> Unit = {}) : AnimationActor {
	contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
	return storeActor(AnimationActor(name, animation, xScale, yScale)).apply(init)
}


inline fun Label.changeSkin(styleName: String, skin: Skin) {
	this.style = skin[styleName, Label.LabelStyle::class.java]
}

inline fun Label.changeSkin(skin: Skin) {
	changeSkin("default",skin)
}
