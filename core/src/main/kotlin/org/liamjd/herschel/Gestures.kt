package org.liamjd.herschel

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxInputAdapter

class Gestures(val camera: OrthographicCamera) : GestureDetector.GestureListener, KtxInputAdapter {
	var velX = 0f
	var velY = 0f
	var flinging = false
	var initialScale = 1f

	override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
//		println("Fling $velocityX,$velocityY,button: $button")
		flinging = true
		velX = camera.zoom * velocityX * 0.5f
		velY = camera.zoom * velocityY * 0.5f
		return false
	}

	override fun zoom(initialDistance: Float, distance: Float): Boolean {
		return false
	}

	override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
//		println("Pan $x,$y, $deltaX,$deltaY")
//		camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0f)
		camera.position.add(-deltaX * camera.zoom, 0f, 0f)
		return false
	}

	override fun pinchStop() {
		TODO("Not yet implemented")
	}

	override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
//		println("Tap $x,$y, count: $count, button: $button")
		return false
	}

	override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
		return false
	}

	override fun longPress(x: Float, y: Float): Boolean {
		TODO("Not yet implemented")
	}

	override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
		return false
	}

	override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
		TODO("Not yet implemented")
	}
}
