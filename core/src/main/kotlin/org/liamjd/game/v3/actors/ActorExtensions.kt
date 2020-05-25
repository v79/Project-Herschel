package org.liamjd.game.v3.actors

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener

inline fun AnimationActor.onEnter(crossinline listener: () -> Unit): InputListener {
	val enterListener = object : InputListener() {
		override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?)= listener()
	}
	this.addListener(enterListener)
	return enterListener
}

inline fun AnimationActor.onExit(crossinline listener: () -> Unit): InputListener {
	val exitListener = object : InputListener() {
		override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) = listener()
	}
	this.addListener(exitListener)
	return exitListener
}
