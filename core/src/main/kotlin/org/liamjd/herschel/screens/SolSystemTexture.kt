package org.liamjd.herschel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxInputAdapter
import org.liamjd.herschel.Herschel
import org.liamjd.herschel.V_HEIGHT_PIXELS
import org.liamjd.herschel.V_WIDTH_PIXELS
import org.liamjd.herschel.services.newgame.GameSetup
import java.lang.String

class SolSystemTexture (herschel: Herschel, stage: Stage, skin: Skin, private val setup: GameSetup) : AbstractGameplayScreen(herschel, stage, skin) {

	var batch: SpriteBatch
	var earth: Sprite
	var camera: OrthographicCamera
	var viewport: Viewport
	var earthAtlas: TextureAtlas
	var currentFrame = 59
	private var currentAtlasKey: kotlin.String = "0001"
	val inputProcessor: InputProcessor
	val backgroundSprite: Sprite


	init {
		batch = SpriteBatch()
		// background
		backgroundSprite = Sprite(Texture(Gdx.files.internal("backgrounds/background-6.png")))

		//earth
		earthAtlas = TextureAtlas(Gdx.files.internal("planets/earth.atlas"))
		val atlasRegion = earthAtlas.findRegion("0001")
		earth = Sprite(atlasRegion)
		earth.setPosition(0f, 0f)
		earth.setSize(100f, 100f)

		Timer.schedule(object : Timer.Task() {
			override fun run() {
				currentFrame--
				if (currentFrame <1 ) currentFrame = 59

				// ATTENTION! String.format() doesnt work under GWT for god knows why...
				currentAtlasKey = String.format("%04d", currentFrame)
				earth.setRegion(earthAtlas.findRegion(currentAtlasKey))
			}
		}
				, 0f, 1 / 30.0f)

		camera = OrthographicCamera()
		viewport = FillViewport(V_WIDTH_PIXELS, V_HEIGHT_PIXELS, camera)
		(viewport as FillViewport).apply()
		camera.position[camera.viewportWidth / 2, camera.viewportHeight / 2] = 0f

		inputProcessor  = object  : KtxInputAdapter{
			override fun keyDown(keycode: Int): Boolean {
				println("keyDown $keycode")
				return false
			}

			override fun keyTyped(character: Char): Boolean {
				println("keyTypes $character")
				return true
			}
			override fun scrolled(amount: Int): Boolean {
				if(amount > 0) {
					camera.zoom -= 0.1f
				} else{
					camera.zoom += 0.1f
				}
//				println("Scrolled $amount")
				return false
			}
		}
	}

	override fun show() {
		// input processor must go in show()
		Gdx.input.inputProcessor = InputMultiplexer(inputProcessor,stage);
	}

	override fun render(delta: Float) {
		camera.update()
//		println("Zoom: ${camera.zoom}")
		Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		batch.projectionMatrix = camera.combined
		batch.begin()
		backgroundSprite.draw(batch)
//		batch.draw(earth,100f,100f)
		earth.draw(batch)
		batch.end()
	}

	override fun dispose() {
		earth.texture.dispose()
		backgroundSprite.texture.dispose()
	}

	override fun resize(width: Int, height: Int) {
		viewport.update(width, height)
		camera.position[camera!!.viewportWidth / 2, camera!!.viewportHeight / 2] = 0f
	}

}
