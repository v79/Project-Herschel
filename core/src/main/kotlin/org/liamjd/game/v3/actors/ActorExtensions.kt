package org.liamjd.game.v3.actors

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener

inline fun Actor.onEnter(crossinline listener: () -> Unit): InputListener {
	val enterListener = object : InputListener() {
		override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
//			println("\t\tActor.onEnter event: $event x,y: $x,$y, pointer: $pointer, fromActor: $fromActor")
			return listener()
		}

		override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
//			println("\t\tActor.touchUp event: $event x,y: $x,$y, pointer: $pointer, button: $button")
			return listener()
		}
	}
	this.addListener(enterListener)
	return enterListener
}

inline fun AnimationActor.onHover(crossinline startFunction: () -> Unit, crossinline endFunction: () -> Unit, crossinline listener: () -> Unit): InputListener {
	val eventListener = object : InputListener() {
		override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
			// if pointer = 1 then we have a click event, which I am not interested in
			if (pointer != 0) {
				hover = true
				startFunction()
				super.enter(event, x, y, pointer, fromActor)
				return listener()
			}
			return
		}

		override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
			// if pointer = 1 then we have a click event, which I am not interested in
			if (pointer != 0) {
				hover = false
				endFunction()
				super.exit(event, x, y, pointer, toActor)
				return listener()
			}
			return
		}
	}
	this.addListener(eventListener)
	return eventListener
}
