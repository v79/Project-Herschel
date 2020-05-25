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
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.actors.onClickEvent
import ktx.actors.onTouchUp
import ktx.actors.plusAssign
import ktx.scene2d.*
import org.liamjd.game.v3.actors.*
import com.badlogic.gdx.utils.Array as GdxArray


class InnerPlanets(game: Version3, stage: Stage, skin: Skin) : AbstractGameplayScreen(game, stage, skin) {

	var year = 2050
	lateinit var yearLabel: Label
	lateinit var planet: Actor
	var planetSprite = Image(Texture(Gdx.files.internal("ui/planet64.png")))
	var planet2Sprite = Image(Texture(Gdx.files.internal("ui/planet64.png")))

	lateinit var animatedPlanet: AnimationActor
	lateinit var animatedPlanet2: AnimationActor

	var shape: ShapeRenderer = ShapeRenderer()

	override fun show() {
		Scene2DSkin.defaultSkin = skin

		// create a custom actor for displaying a texture image
		val backgroundActor = TextureActor(Texture(Gdx.files.internal("ui/starfield.png")))

		val bluePlanetActor = TextureActor(Texture(Gdx.files.internal("ui/planet64.png")))

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

		bluePlanetActor.name = "Blue planet"
		bluePlanetActor.setPosition(stage.width - 400f, (stage.height / 2f))
		// still don't understand this
		bluePlanetActor.addListener(object : ClickListener() {
			override fun clicked(event: InputEvent, x: Float, y: Float) {
				println("Blue planet clicked")
			}
		})
		val planet1Label = Label("Small planet",skin)
		val planet2Label = Label("Large planet",skin)
//		planet1Label.setPosition(animatedPlanet.x,stage.height - 400f)
//		planet2Label.setPosition(animatedPlanet2.x,stage.height - 400f)


		// add an actor to the stage directly
		stage += backgroundActor
//		stage += planet1Label
//		stage += planet2Label
		stage += bluePlanetActor

		// horizontal centre line
		stage.addActor(object : Actor() {
			override fun draw(batch: Batch?, parentAlpha: Float) {
				if(batch != null) {
					batch.end()
					shape.setProjectionMatrix(stage.camera.combined)
					shape.begin(ShapeType.Line)
					shape.setColor(Color.RED)
					shape.rect(0f, stage.height / 2f, stage.width, 1f)
					shape.end()
					batch.begin()
				}
			}
		})

		stage.actors {

			animation(name = "Planet 1", animation =  Animation<TextureRegion>(0.5f, redPlanetFrames) ,xScale = 0.2f, yScale = 0.2f) {
				setPosition(stage.width - 600f, ((stage.height / 2) - (redPlanetHeight * this.yScale) / 2))
				onClick {
					println("Clicked on ${this.name}")
				}
				onEnter {
					zoom()
				}
				onExit {
					resetZoom()
				}
			}

			animation(name = "Planet 2", animation =  Animation<TextureRegion>(0.5f, redPlanetFrames) ,xScale = 0.4f, yScale = 0.4f) {
				setPosition(stage.width - 300f, ((stage.height / 2) - (redPlanetHeight * this.yScale) / 2))
				onClickEvent { event, actor ->
					println("onClick x,y: $x,$y - stageX,stageY: ${event.stageX},${event.stageY}")
				}
//				onClick {
//					println("Clicked on ${this.name}")
//				}
				onEnter {
					println("onEnter x,y $x,$y")
					zoom()
				}
				onTouchUp { println("onTouchUp x,y $x,$y") }
				onExit {
					resetZoom()
				}
			}

			table {
//				debug()
				setFillParent(true) // for the primary layout
				align(Align.top)

				padding(10f, 10f, 5f, 5f)

				textButton("Main Menu") { cell ->
					cell.expandX()
					cell.align(Align.left) // pull to the left
					onClick {
						hide()
						game.setScreen<MainMenu>()
					}
				}

				label("Herschel", style = "title") { cell ->
					val titleWidth = this.width
					cell.padLeft((stage.width / 2f) - titleWidth * 2)
					cell.padRight((stage.width / 2f) - titleWidth * 2)
					cell.align(Align.center)
				}

				yearLabel = label("Year $year") { cell ->
					cell.expandX() // and pad it out
					cell.minWidth(100f)
					cell.align(Align.right) // pull to the right
				}

				row().expandY().align(Align.bottom) // create a new row, push it to the bottom

				textButton("Next turn") { cell ->
					cell.align(Align.bottomRight)
					cell.colspan(3)
					onClick {
						year += 1
						println(year)
					}
				}
			}
		}
	}


	override fun render(delta: Float) {
		yearLabel.setText("Year $year")
		super.render(delta)
	}

	override fun dispose() {
		stage.dispose()
		skin.dispose()
	}
}








