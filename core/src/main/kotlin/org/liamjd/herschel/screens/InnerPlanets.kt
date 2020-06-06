package org.liamjd.herschel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
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
	lateinit var defaultAnimation: Animation<TextureRegion>

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

		// Load the sprite sheet form a TextureAtlas
		val defaultTextureAtlas = TextureAtlas(Gdx.files.internal("planets/earth.atlas"))
		defaultAnimation = Animation<TextureRegion>(0.1f, defaultTextureAtlas.regions, PlayMode.LOOP)


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

		stage.actors {

			val title = Label("Herschel", screenSkin, "title")
			title.setPosition(stage.width / 2f, stage.height - 20f, Align.center)

//			stage += title

			// primary layout
			table {
				setFillParent(true) // for the primary layout
//				debug()
				align(Align.top)

				table {
					background = screenSkin.getDrawable("ui-background-blue")
//					debug()

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
				titleTable.add(windowCloseButton)
				windowCloseButton.onClick { isVisible = false }
			}

			generatePlanets(solarSystem.planets, defaultAnimation.getKeyFrame(0f).regionHeight, planetWindow)
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

	private fun @Scene2dDsl StageWidget.generatePlanets(planetList: List<Planet>, textureHeight: Int, planetWindow: KWindow) {
		planetList.forEachIndexed { index, planet ->
			val seperator = stage.width / planetList.size

			val drawSize = MathUtils.clamp(planet.scale / 4f,0.05f,0.3f) + planet.scale / 10000
			val animation = if(planet.textureAtlas != null && planet.textureAtlas!!.isNotEmpty()) {
				Animation<TextureRegion>(0.1f, TextureAtlas(Gdx.files.internal(planet.textureAtlas)).regions, PlayMode.LOOP)
			} else {
				defaultAnimation
			}
			planet(name = planet.name, animation = animation, xScale = drawSize, yScale = drawSize) {
				setPosition((index.toFloat() * seperator), ((stage.height / 2) - (textureHeight * this.yScale) / 2))
				onHover(onEnterFunction = { zoom() }, onExitFunction = { resetZoom() }) {}
				onClick {
					println("onClick ${planet.name}")
					planetWindow.isVisible = false
					planetWindow.clearChildren()
					planetWindow.titleLabel.setText("${planet.name}")
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
