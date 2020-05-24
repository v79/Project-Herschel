package org.liamjd.game.v3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.assets.toInternalFile
import ktx.inject.Context
import ktx.inject.register
import ktx.style.add
import ktx.style.defaultStyle
import ktx.style.label
import ktx.style.textButton

class Version3 : KtxGame<Screen>() {

	private val context = Context()
	private val WIDTH = 640f
	private val HEIGHT = 480f

	override fun create() {
		val skin = createSkin()

		// set up dependency injection
		context.register {
			bindSingleton(this@Version3)
			bindSingleton(SpriteBatch())
			bindSingleton(Stage((FitViewport(WIDTH, HEIGHT))))
			bindSingleton(skin)
			// The camera ensures we can render using our target resolution of 800x480
			//    pixels no matter what the screen resolution is.
			bindSingleton(OrthographicCamera().apply { setToOrtho(false, WIDTH, HEIGHT) })
			// use DI to add each screen
			addScreen(MainMenu(inject(), inject(), inject()))
			addScreen(InnerPlanets(inject(), inject(), inject()))
		}
		// set the launch screen
		setScreen<MainMenu>()
	}

	override fun dispose() {
		context.dispose()
		super.dispose()
	}

	/**
	 * Initialise the project skin
	 */
	fun createSkin(): Skin {
		// load skin definition file
		val skin = Skin(Gdx.files.internal("ui/skin.json")).apply {
			// define font called "title". There should be be a .fnt file in the assets folder
			// and a corresponding .png image file in the raw/ui folder
			// running 'gradle pack' will add "title" to the skin.atlas file, making it available for use
			add("bahnschrift", BitmapFont("ui/bahnschrift.fnt".toInternalFile(), this.getRegion("bahnschrift")))
			// associate "title" labels with the "title" font, from above
			label("title") {
				font = this@apply.getFont("bahnschrift")
			}
		}
		return skin
	}

}
