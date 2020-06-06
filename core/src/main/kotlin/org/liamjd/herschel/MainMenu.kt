package org.liamjd.herschel

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.app.KtxScreen
import ktx.scene2d.*
import org.liamjd.herschel.actors.TextureActor
import org.liamjd.herschel.extensions.changeSkin
import org.liamjd.herschel.extensions.onEnter
import org.liamjd.herschel.extensions.onExit
import org.liamjd.herschel.screens.NewGame

class MainMenu(private val herschel: Herschel,
			   private val stage: Stage, private val skin: Skin) : KtxScreen {

	override fun show() {

		// override the defaultSkin on all items, so I don't need to pass skin = skin to every actor
		Scene2DSkin.defaultSkin = skin

		// create a custom actor for displaying a texture image
		val backgroundActor = TextureActor(Texture(Gdx.files.internal("screens/MainMenu/background.png")))

		// add an actor to the stage directly
		stage += backgroundActor

		// in this code block, build a set of actors
		stage.actors {
			label("Herschel", style = "title") {
				setPosition(stage.width / 2f, stage.height - 64f, Align.center)
			}
			table() {
				defaults().padBottom(32f)
				setFillParent(true)
				label("New Game","mainMenu") {
					onEnter {
						changeSkin("mainMenuHover",this@MainMenu.skin)
					}
					onExit {
						changeSkin("mainMenu",this@MainMenu.skin)
					}
					// tooltips are causing problems with my background
					textTooltip("Start a new game of Herschel", skin = this@MainMenu.skin, style = "simple")
					onClick {
						stage.clear()
						herschel.setScreen<NewGame>()
					}
				}.cell(row = true)
				label("Load Game","mainMenu") {
					onEnter {
						changeSkin("mainMenuHover",this@MainMenu.skin)
					}
					onExit {
						changeSkin("mainMenu",this@MainMenu.skin)
					}
				}.cell(row = true)
				label("Settings","mainMenu") {
					onEnter {
						changeSkin("mainMenuHover",this@MainMenu.skin)
					}
					onExit {
						changeSkin("mainMenu",this@MainMenu.skin)
					}
				}.cell(row = true)
				label("Quit","mainMenu") {
					onEnter {
						changeSkin("mainMenuHover",this@MainMenu.skin)
					}
					onExit {
						changeSkin("mainMenu",this@MainMenu.skin)
					}
					onClick {
						Gdx.app.exit()
					}
				}.cell(row = true)
				pack()
			}
		}
		// all the stage to respond to input (essential!)
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
}


