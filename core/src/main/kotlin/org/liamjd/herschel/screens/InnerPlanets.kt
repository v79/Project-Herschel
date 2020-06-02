package org.liamjd.herschel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.*
import org.liamjd.herschel.Herschel
import org.liamjd.herschel.MainMenu
import org.liamjd.herschel.extensions.animation
import org.liamjd.herschel.extensions.onHover
import org.liamjd.herschel.models.GameState
import kotlin.random.Random
import com.badlogic.gdx.utils.Array as GdxArray

data class Planet(val name: String, val x: Float, val scale: Float, val color: Color?)

class InnerPlanets(herschel: Herschel, stage: Stage, skin: Skin) : AbstractGameplayScreen(herschel, stage, skin) {

	lateinit var gameState: GameState

	lateinit var yearLabel: Label
	lateinit var planet: Actor
	var backgroundActor: Image = Image(Texture(Gdx.files.internal("ui/starfield.png")))
	lateinit var gameMenu: KDialog
	lateinit var blackOverlayImage: Actor
	lateinit var windowCloseButton: Actor
	var modalVisible = false

	var circleHighlight: ShapeRenderer = ShapeRenderer()

	init {
		// create a custom actor for displaying a texture image
		backgroundActor.setOrigin(stage.width / 2f, stage.height / 2f)
		backgroundActor.setScale(2f)
	}

	override fun show() {
		gameState = herschel.state
		Scene2DSkin.defaultSkin = screenSkin

		blackOverlayImage = Image(screenSkin.getRegion("white-overlay"))
		blackOverlayImage.setOrigin(0f,0f)
		blackOverlayImage.setSize(stage.width,stage.height)
		blackOverlayImage.isVisible = false
		blackOverlayImage.setColor(0f,0f,0f,0.75f)

		windowCloseButton = Image(screenSkin.getRegion("grey_crossGrey"))
		windowCloseButton.setSize(24f,24f)

		// Load the sprite sheet as a Texture
		val redPlanetTextureSheet = Texture(Gdx.files.internal("ui/red-planet-spritesheet.png"))
		val redPlanetHeight = redPlanetTextureSheet.height / 4
		// Use the split utility method to create a 2D array of TextureRegions. This is
		// possible because this sprite sheet contains frames of equal size and they are
		// all aligned.
		val tmpRegion = TextureRegion.split(redPlanetTextureSheet, redPlanetTextureSheet.width / 5, redPlanetHeight)
		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.
		val redPlanetFrames = GdxArray<TextureRegion>(5 * 4)

		for (i in 0 until 4) {
			for (j in 0 until 2) {
				redPlanetFrames.add(tmpRegion[i][j])
			}
		}

		// add an actor to the stage directly
		stage += backgroundActor


		// horizontal centre line
		stage.addActor(object : Actor() {
			override fun draw(batch: Batch?, parentAlpha: Float) {
				if (batch != null) {
					batch.end()
					circleHighlight.projectionMatrix = stage.camera.combined
					circleHighlight.begin(ShapeType.Line)
					circleHighlight.color = Color.LIME
					circleHighlight.rect(0f, stage.height / 2f, stage.width, 1f)
					circleHighlight.rect(stage.width / 2f, 0f, 1f, stage.height)
					circleHighlight.end()
					batch.begin()
				}
			}
		})

		val planetList = mutableListOf<Planet>()
		val planetCount = 4
		val spacing = stage.width / planetCount
		val r = Random(System.currentTimeMillis())

		// screen width = 1024
		// average spacing = (1024/planetCount) = 170
		// move to the left a bit = -100f
		// 70,240,411,582,752
		val spacings = arrayOf(50f, 125f, 275f, 500f, 650f, 850f)
		for (p: Int in 0 until planetCount) {
			val x = (spacing * p)
			val rScale = r.nextFloat()
			val planetScale = (p + 1) * (0.1f + (rScale / 100))
			println("Planet $p should be at spacings[p]=${spacings[p]}. Random scale factor=$rScale, gives planet size: $planetScale")
			planetList.add(Planet("Planet $p", spacings[p], planetScale, null))
		}

		stage.actors {

			val title = Label("Herschel", screenSkin, "title")
			title.setPosition(stage.width / 2f, stage.height - 20f, Align.center)

			stage += title

			// primary layout
			table {
				setFillParent(true) // for the primary layout
				align(Align.top)

				padding(10f, 10f, 5f, 5f)

				textButton("Main Menu") { cell ->
					cell.align(Align.left)
					onClick {
						gameMenu.pack()
						gameMenu.zIndex = stage.actors.size // this is relative, not absolute
						gameMenu.setPosition(stage.width / 2f - (gameMenu.width / 2f), stage.height / 2f - (gameMenu.height / 2f))
						showBlackOverlay()
						gameMenu.isVisible = true
					}
				}
				horizontalGroup { cell ->
					cell.expandX()
					cell.align(Align.left)
					imageButton(style = "sciEngButton") {
						onClick {
							println("Clicked on engineering science")
						}
					}
					imageButton(style = "sciChmButton") {
						onClick {
							println("Clicked on chemistry science")
						}
					}
				}
				yearLabel = label("Year ${gameState.year}") { cell ->
					cell.align(Align.right)
				}

				row().expandY().align(Align.bottom) // create a new row, push it to the bottom

				textButton("Next turn") { cell ->
					cell.colspan(4)
					cell.expandX()
					cell.expandY()
					cell.align(Align.bottomRight)
					onClick {
						gameState.endTurn()
						backgroundActor.rotateBy(1f)
					}
				}
			}

			// additional actors
			gameMenu = buildGameMenu()

			val planetWindow = window("Planet window","blue").apply {
				isModal = false
				isMovable = true
				isVisible = false
			}

			generatePlanets(planetList, redPlanetFrames, redPlanetHeight, planetWindow)
		}

		// black overlay for modal dialogs
		stage+= blackOverlayImage

	}

	private fun @Scene2dDsl StageWidget.buildGameMenu(): KDialog {
		 return dialog("Game Menu", style = "blue") {
			isMovable = false
			isVisible = false
			verticalGroup {
				textButton("Continue") {
					onClick {
						hideBlackOverlay()
						this@dialog.isVisible = false
					}
				}
				textButton("Save")
				textButton("Options")
				textButton("Return to main menu") {
					onClick {
						this@InnerPlanets.hide()
						herschel.setScreen<MainMenu>()
					}
				}
			}.space(10f)
		}
	}

	private fun @Scene2dDsl StageWidget.generatePlanets(planetList: MutableList<Planet>, redPlanetFrames: com.badlogic.gdx.utils.Array<TextureRegion>, redPlanetHeight: Int, planetWindow: KWindow) {
		planetList.forEach { planet ->
			animation(name = planet.name, animation = Animation<TextureRegion>(0.5f, redPlanetFrames), xScale = planet.scale, yScale = planet.scale) {
				setPosition(planet.x, ((stage.height / 2) - (redPlanetHeight * this.yScale) / 2))
				onHover(onEnterFunction = { zoom() }, onExitFunction = { resetZoom() }) {}
				onClick {
					println("onClick $planet")
					planetWindow.isVisible = false
					planetWindow.clearChildren()
					planetWindow.titleLabel.setText("${planet.name} is lovely")

					planetWindow.titleTable.add(windowCloseButton)
					windowCloseButton.onClick { planetWindow.isVisible = false }

					planetWindow.verticalGroup {
						label("Programmatically added label ${planet.name}")
						label("Second added label ${planet.name}")
					}
					planetWindow.pack()
					planetWindow.isVisible = true
				}
			}
		}
	}

	private fun showBlackOverlay() {
		modalVisible = true
		blackOverlayImage.isVisible = true
	}
	private fun hideBlackOverlay() {
		modalVisible = false
		blackOverlayImage.isVisible = false
	}


	override fun render(delta: Float) {
		stage.act(delta)
		yearLabel.setText("${gameState.era.name} Year ${gameState.year}")
		stage.draw()
	}

	override fun dispose() {
		stage.dispose()
		screenSkin.dispose()
	}
}
