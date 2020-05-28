package org.liamjd.game.v3.sceneExtensions

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

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

inline fun Label.changeSkin(styleName: String, skin: Skin) {
	this.style = skin[styleName, Label.LabelStyle::class.java]
}

inline fun Label.changeSkin(skin: Skin) {
	changeSkin("default",skin)
}
