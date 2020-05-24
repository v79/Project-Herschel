package org.liamjd.game.v3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import ktx.app.KtxScreen
import ktx.scene2d.*
import org.liamjd.game.v3.actors.TextureActor

@Deprecated("Use MainMenu instead",replaceWith = ReplaceWith("MainMenu"))
class MainSceen(private val game: Version3,
private val stage: Stage, private val skin: Skin) : KtxScreen {



	init {

		val backgroundTexture = Texture(Gdx.files.internal("screens/MainMenu/background.png"))
		val backgroundActor = TextureActor(backgroundTexture)


		val title = scene2d.label("Welcome to Herschel",skin = skin)
		title.setPosition(stage.width /2f , stage.height -64f, Align.center)





		val button = TextButton("Click me!", skin).apply {
			pad(8f)
			addListener(object : ChangeListener() {
				override fun changed(event: ChangeEvent, actor: Actor) {
					this@apply.setText("Clicked.")
				}
			})
		}

		val window = Window("Example screen", skin, "border").apply {
			defaults().pad(4f)
			add("This is a simple Scene2D view.").row()
			add(button)
			addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)))
			pack()
		}
		window.setPosition(stage.width / 2f - window.width / 2f,
				stage.height / 2f - window.height / 2f)

		stage.addActor(backgroundActor)
		stage.addActor(title)
		stage.actors {
			buildMainMenu()
		}
//		stage.addActor(window)
		Gdx.input.inputProcessor = stage
	}

	override fun render(delta: Float) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		stage.act(Gdx.graphics.deltaTime)


		stage.draw()
	}

	override fun resize(width: Int, height: Int) {
		stage.viewport.update(width, height)
	}

	override fun dispose() {
		skin.dispose()
	}

	fun buildMainMenu() {
		scene2d {
			label("New Game",skin = skin)
			label("Load Game",skin = skin)
			label("Settings",skin=skin)
			label("Quit",skin = skin)
		}.pack()
	}
}

/*class TextureActor(private val texture: Texture) : Actor() {
	override fun draw(batch: Batch, parentAlpha: Float) {
		batch.draw(texture, x, y)
	}
}*/
