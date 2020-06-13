package org.liamjd.herschel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.app.KtxScreen
import ktx.scene2d.KTableWidget
import org.liamjd.herschel.Herschel

abstract class AbstractGameplayScreen(val herschel: Herschel,
									  val stage: Stage, val screenSkin: Skin) : KtxScreen {

	override fun render(delta: Float) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		stage.act(Gdx.graphics.deltaTime)
		Gdx.graphics.setTitle("Herschel at ${Gdx.graphics.framesPerSecond} fps")
		stage.draw()
	}

	override fun resize(width: Int, height: Int) {
		stage.viewport.update(width, height)
	}

	fun KTableWidget.padding(left: Float = 0f, right: Float = 0f, top: Float = 0f, bottom: Float = 0f) {
		padLeft(left)
		padRight(right)
		padTop(top)
		padBottom(bottom)
	}

	override fun hide() {
		stage.root.clearChildren()
	}

}

inline fun Batch.use(action: () -> Unit) {
	begin()
	action()
	end()
}
