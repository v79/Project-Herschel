package org.liamjd.game.v3

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
import org.liamjd.game.v3.actors.AnimationActor
import org.liamjd.game.v3.actors.animation
import org.liamjd.game.v3.actors.onHover
import kotlin.random.Random
import com.badlogic.gdx.utils.Array as GdxArray


data class Planet(val name: String, val x: Float, val scale: Float, val color: Color?)

class InnerPlanets(game: Version3, stage: Stage, skin: Skin) : AbstractGameplayScreen(game, stage, skin) {

	var year = 2050
	lateinit var yearLabel: Label
	lateinit var planet: Actor
	var planetSprite = Image(Texture(Gdx.files.internal("ui/planet64.png")))
	var planet2Sprite = Image(Texture(Gdx.files.internal("ui/planet64.png")))

	lateinit var animatedPlanet: AnimationActor
	lateinit var animatedPlanet2: AnimationActor
	lateinit var backgroundActor: Image

	var shape: ShapeRenderer = ShapeRenderer()

	override fun show() {
		Scene2DSkin.defaultSkin = screenSkin


		// create a custom actor for displaying a texture image
		backgroundActor = Image(Texture(Gdx.files.internal("ui/starfield.png")))
		backgroundActor.setOrigin(stage.width / 2f, stage.height / 2f)
		backgroundActor.setScale(2f)


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

		val planet1Label = Label("Small planet", screenSkin)
		val planet2Label = Label("Large planet", screenSkin)
//		planet1Label.setPosition(animatedPlanet.x,stage.height - 400f)
//		planet2Label.setPosition(animatedPlanet2.x,stage.height - 400f)


		// add an actor to the stage directly
		stage += backgroundActor
//		stage += planet1Label
//		stage += planet2Label

		// horizontal centre line
		stage.addActor(object : Actor() {
			override fun draw(batch: Batch?, parentAlpha: Float) {
				if (batch != null) {
					batch.end()
					shape.projectionMatrix = stage.camera.combined
					shape.begin(ShapeType.Line)
					shape.color = Color.LIME
					shape.rect(0f, stage.height / 2f, stage.width, 1f)
					shape.rect(stage.width / 2f, 0f, 1f, stage.height)
					shape.end()
					batch.begin()
				}
			}
		})

		val planetList = mutableListOf<Planet>()
		val planetCount = 6
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

			val planetWindow = window("Planet window").apply {
				isModal = true
				isMovable = true
				isVisible = false
			}

			planetList.forEach { planet ->
				animation(name = planet.name, animation = Animation<TextureRegion>(0.5f, redPlanetFrames), xScale = planet.scale, yScale = planet.scale) {
					setPosition(planet.x, ((stage.height / 2) - (redPlanetHeight * this.yScale) / 2))
					onHover(startFunction = { zoom() }, endFunction = { resetZoom() }) {}
					onClick {
						println("onClick $planet")
						planetWindow.titleLabel.setText(planet.name)
						planetWindow.isVisible = true
					}
				}
			}


			val title = Label("Herschel", screenSkin, "title")
			title.setPosition(stage.width / 2f, stage.height - 20f, Align.center)

			stage += title

			table {
				debug()
				setFillParent(true) // for the primary layout
				align(Align.top)

				padding(10f, 10f, 5f, 5f)

				textButton("Main Menu") { cell ->
					cell.align(Align.left)
					onClick {
						hide()
						game.setScreen<MainMenu>()
					}
				}
				container { cell ->
					cell.expandX()
					cell.align(Align.left)
					imageButton(style = "sciEngButton") {
						onClick {
							println("Clicked on engineering science")
						}
					}

				}
				yearLabel = label("Year $year") { cell ->
					cell.align(Align.right)
				}

				row().expandY().align(Align.bottom) // create a new row, push it to the bottom

				textButton("Next turn") { cell ->
					cell.colspan(4)
					cell.expandX()
					cell.expandY()
					cell.align(Align.bottomRight)
					onClick {
						year += 100
						backgroundActor.rotateBy(1f)
					}
				}
			}
		}
	}


	override fun render(delta: Float) {
		stage.act(delta)

		yearLabel.setText("Year $year")
		super.render(delta)
	}

	override fun dispose() {
		stage.dispose()
		screenSkin.dispose()
	}
}










