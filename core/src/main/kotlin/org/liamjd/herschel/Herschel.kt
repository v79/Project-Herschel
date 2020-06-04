package org.liamjd.herschel

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.inject.Context
import ktx.inject.register
import org.liamjd.herschel.models.GameState
import org.liamjd.herschel.screens.InnerPlanets
import org.liamjd.herschel.screens.NewGame
import org.liamjd.herschel.services.newgame.GameSetup

class Herschel : KtxGame<Screen>() {

	private val context = Context()
	private val WIDTH = 1024f
	private val HEIGHT = 768f
	lateinit var state: GameState	// global object

	override fun create() {
		val skin = createSkin()

		// set up dependency injection
		context.register {
			bindSingleton(this@Herschel)
			bindSingleton(SpriteBatch())
			bindSingleton(Stage((FitViewport(WIDTH, HEIGHT))))
			bindSingleton(skin)
			// The camera ensures we can render using our target resolution of 800x480
			//    pixels no matter what the screen resolution is.
			bindSingleton(OrthographicCamera().apply { setToOrtho(false, WIDTH, HEIGHT) })
			bindSingleton(GameSetup())
			// use DI to add each screen
			addScreen(MainMenu(inject(), inject(), inject()))
			addScreen(InnerPlanets(inject(), inject(), inject(),inject()))
			addScreen(NewGame(inject(),inject(),inject(),inject()))
		}
		// set the launch screen
		setScreen<MainMenu>()
	}

	override fun dispose() {
		context.dispose()
	}

	/**
	 * Initialise the project skin
	 */
	fun createSkin(): Skin {
		// load skin definition file
//		val atlas = TextureAtlas(Gdx.files.internal("ui/skin.atlas"))
//		val atlas = TextureAtlas(Gdx.files.internal("ui/plain-james-skin.atlas"))
		//.apply {
//		val skin = Skin(Gdx.files.internal("ui/skin.json")).apply {
//			addRegions(atlas)
			// define font called "title". There should be be a .fnt file in the assets folder
			// and a corresponding .png image file in the raw/ui folder
			// running 'gradle pack' will add "title" to the skin.atlas file, making it available for use
//			add("bahnschrift", BitmapFont("ui/bahnschrift.fnt".toInternalFile(), this.getRegion("bahnschrift")))
			// associate "title" labels with the "title" font, from above
//			label("title") {
//				font = this@apply.getFont("bahnschrift")
//			}

//			val sciEngDrawable: Drawable = TextureRegionDrawable(TextureRegionDrawable(this.getRegion("sci-eng")))
//			add("sci-eng",sciEngDrawable)
//			imageButton("sci-eng-button") {
//				ImageButton.ImageButtonStyle(sciEngDrawable)
//			}
//		}
		return Skin(Gdx.files.internal("ui/plain-james-skin.json"))
	}

}
