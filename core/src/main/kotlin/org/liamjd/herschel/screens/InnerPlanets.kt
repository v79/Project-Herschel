package org.liamjd.herschel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.MathUtils
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
import org.liamjd.herschel.extensions.onHover
import org.liamjd.herschel.extensions.planet
import org.liamjd.herschel.models.GameState
import org.liamjd.herschel.models.solarsystems.Planet
import org.liamjd.herschel.models.solarsystems.SolarSystem
import org.liamjd.herschel.services.newgame.GameSetup
import org.liamjd.herschel.uicomponents.scienceIcon
import kotlin.random.Random
import com.badlogic.gdx.utils.Array as GdxArray


class InnerPlanets(herschel: Herschel, stage: Stage, skin: Skin, setup: GameSetup) : AbstractGameplayScreen(herschel, stage, skin) {

	lateinit var gameState: GameState

	lateinit var yearLabel: Label
	lateinit var planet: Actor
	var backgroundActor: Image = Image(Texture(Gdx.files.internal("ui/starfield.png")))
	lateinit var gameMenu: KDialog
	lateinit var blackOverlayImage: Actor
	lateinit var windowCloseButton: Actor
	var modalVisible = false
	val solarSystem: SolarSystem
	var circleHighlight: ShapeRenderer = ShapeRenderer()

	init {
		// create a custom actor for displaying a texture image
		backgroundActor.setOrigin(stage.width / 2f, stage.height / 2f)
		backgroundActor.setScale(2f)

		solarSystem = setup.loadSolarSystem()
	}

	override fun show() {
		gameState = herschel.state
		Scene2DSkin.defaultSkin = screenSkin

		blackOverlayImage = Image(screenSkin.getRegion("white-overlay"))
		blackOverlayImage.setOrigin(0f, 0f)
		blackOverlayImage.setSize(stage.width, stage.height)
		blackOverlayImage.isVisible = false
		blackOverlayImage.setColor(0f, 0f, 0f, 0.75f)

		windowCloseButton = Image(screenSkin.getRegion("grey_crossGrey"))
		windowCloseButton.setSize(24f, 24f)

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

		val spacing = stage.width / solarSystem.numPlanets
		val r = Random(System.currentTimeMillis())

		// screen width = 1024
		// average spacing = (1024/planetCount) = 170
		// move to the left a bit = -100f
		// 70,240,411,582,752
		val spacings = arrayOf(50f, 125f, 275f, 500f, 650f, 850f)
//		for (p: Int in 0 until solarSystem.numPlanets) {
//			val x = (spacing * p)
//			val rScale = r.nextFloat()
//			val planetScale = (p + 1) * (0.1f + (rScale / 100))
////			planetList.add(Planet("Planet $p", spacings[p], planetScale, Color.RED.toIntBits(),isDwarf = false,rings = 0,moons = Random(System.currentTimeMillis()).nextInt(0,3)))
//		}

		stage.actors {

			val title = Label("Herschel", screenSkin, "title")
			title.setPosition(stage.width / 2f, stage.height - 20f, Align.center)

//			stage += title

			// primary layout
			table {
				setFillParent(true) // for the primary layout
				debug()
//				padding(10f, 10f, 5f, 5f)
				align(Align.top)
//

				table {
					setBackground(screenSkin.getDrawable("ui-background-blue"))
					debug()

					textButton("Main Menu") { cell ->
						cell.padLeft(10f)
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
						space(3f)
//						cell.expandX()
						cell.align(Align.left)

						gameState.scienceIcons.forEach { science ->
							scienceIcon(science) {
								scaleIcon(image, science.iconScale)
								val tt = textTooltip(science.tooltip, style = "simple")
								onClick {
									println("Clicked on ${science.name} with value ${science.value}")
									science.value++
									this.label.setText(science.value.toString())
									tt.actor.setText("${science.name} now at ${science.value}")
								}

							}
						}
					}
					yearLabel = label("Year ${gameState.year}") { cell ->
						cell.align(Align.right)
						cell.padRight(10f)
					}
				}
				row().expandY().align(Align.bottom) // create a new row, push it to the bottom
				align(Align.bottomRight)
				textButton("Next turn") { cell ->
//					cell.colspan(4)
//					cell.padRight(10f)
					cell.expandX()
					cell.expandY()
					cell.align(Align.bottomRight)
					onClick {
						gameState.endTurn()
						backgroundActor.rotateBy(-1f)
					}
				}
			}

			// additional actors
			gameMenu = buildGameMenu()

			val planetWindow = window("Planet window", "blue").apply {
				isModal = false
				isMovable = true
				isVisible = false
			}

			generatePlanets(solarSystem.planets, redPlanetFrames, redPlanetHeight, planetWindow)
		}

		// black overlay for modal dialogs
		stage += blackOverlayImage

	}

	private fun KImageTextButton.scaleIcon(image: Image, scale: Float, align: Int = Align.center) {
		image.scaleBy(scale) // scale first
		pack()    // then pack
		image.setOrigin(align) // and finally center. The order matters!
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

	private fun @Scene2dDsl StageWidget.generatePlanets(planetList: List<Planet>, redPlanetFrames: com.badlogic.gdx.utils.Array<TextureRegion>, redPlanetHeight: Int, planetWindow: KWindow) {
		planetList.forEachIndexed { index, planet ->
			val seperator = stage.width / planetList.size

			val drawSize = MathUtils.clamp(planet.scale / 4f,0.05f,0.3f) + planet.scale / 10000
			println("${planet.name} x: ${index.toFloat() * seperator}, scale: ${planet.scale}, drawn at $drawSize}")
			planet(name = planet.name, animation = Animation<TextureRegion>(0.5f, redPlanetFrames), xScale = drawSize, yScale = drawSize) {
				setPosition((index.toFloat() * seperator), ((stage.height / 2) - (redPlanetHeight * this.yScale) / 2))
				onHover(onEnterFunction = { zoom() }, onExitFunction = { resetZoom() }) {}
				onClick {
					println("onClick ${planet.name}")
					planetWindow.isVisible = false
					planetWindow.clearChildren()
					planetWindow.titleLabel.setText("${planet.name}")

					planetWindow.titleTable.add(windowCloseButton)
					windowCloseButton.onClick { planetWindow.isVisible = false }

					planetWindow.verticalGroup {
						val sb: StringBuilder = StringBuilder()
						label("Size: ${planet.scale}")
						planet.baseScience.forEach { (s, u) -> sb.append("${s.name}: $u ") }
						label("Science: $sb")
						sb.clear()
						planet.modifiers.forEach { (k, v) -> sb.append("${k}: ${v}\n") }
						label("$sb")

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
		gameState.scienceIcons.forEach {

		}
		stage.draw()
	}

	override fun dispose() {
		stage.dispose()
		screenSkin.dispose()
	}
}
