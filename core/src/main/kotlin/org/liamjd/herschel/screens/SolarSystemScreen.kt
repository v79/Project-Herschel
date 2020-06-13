package org.liamjd.herschel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.actors.plusAssign
import ktx.app.KtxInputAdapter
import ktx.collections.GdxArray
import org.liamjd.herschel.*
import org.liamjd.herschel.models.Era
import org.liamjd.herschel.models.solarsystems.Planet
import org.liamjd.herschel.models.solarsystems.SolarSystem
import org.liamjd.herschel.services.newgame.GameSetup
import org.liamjd.herschel.uicomponents.AnimatedSprite
import org.liamjd.herschel.uicomponents.PlanetAnimation
import org.liamjd.herschel.utilities.logger


class SolarSystemScreen(herschel: Herschel, stage: Stage, skin: Skin, private val setup: GameSetup) : AbstractGameplayScreen(herschel, stage, skin) {

	companion object {
		private val log = logger<SolarSystemScreen>()
	}

	private val hud: GameHUD3 = GameHUD3(screenSkin = screenSkin, herschel = herschel)
	private var gridRenderer: ShapeRenderer = ShapeRenderer()
	private val drawGrid = false
	private val systemBatch = SpriteBatch()
	private val camera: OrthographicCamera
	private val systemViewPort: Viewport
	private val inputProcessor: InputProcessor
	private val gestureDetector: Gestures
	private val cameraCentre = Vector3(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0f)
	private val minZoom = 0.3f // small number is closely zoomed in
	private val maxZoom = 8f // scroll up to zoom out

	private val earthAnimation = AnimatedSprite("planets/jupiter.atlas", 60f)
	private var elapsedTime = 0f
	private val solarSystem: SolarSystem
	private val planets: GdxArray<PlanetAnimation>
	private val homePlanet: Planet
	private val homePlanetPosition: Vector3

	private val font: BitmapFont
	private var debugText: String = ""

	private val backgroundStage: Stage = Stage() // separate stage for the background only
	private val mousePosition = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)

	private var selectedPlanetAnimation : PlanetAnimation? = null

	init {
		val aspectRatio = (Gdx.graphics.width / Gdx.graphics.height).toFloat()
		camera = OrthographicCamera(WORLD_WIDTH * aspectRatio, WORLD_HEIGHT.toFloat())
		gestureDetector = Gestures(camera)

		systemViewPort = FillViewport(V_WIDTH_PIXELS, V_HEIGHT_PIXELS, camera)
		(systemViewPort as FillViewport).apply()

		solarSystem = loadSolarSystem()
		planets = loadPlanets()
		homePlanet = solarSystem.planets[2]
		homePlanetPosition = Vector3(planets[3].xPos, WORLD_HEIGHT / 2f, 0f)
		camera.position.set(homePlanetPosition)

		font = BitmapFont(Gdx.files.internal("ui/oxanium-18.fnt"))

		val keyboardMoveSpeed = WORLD_WIDTH / 100f
		inputProcessor = object : KtxInputAdapter {
			override fun keyDown(keycode: Int): Boolean {
				when (keycode) {
					Input.Keys.A, Input.Keys.LEFT -> {
						println("Left (a) pressed")
						if (camera.position.x > 0) camera.translate(-keyboardMoveSpeed, 0f)
					}
					Input.Keys.D, Input.Keys.RIGHT -> {
						println("Right (d) pressed")
						if (camera.position.x <= WORLD_WIDTH) camera.translate(keyboardMoveSpeed, 0f)
					}
					Input.Keys.HOME -> {
						println("Home pressed")
						camera.position.set(homePlanetPosition)
						camera.zoom = 1f
					}
				}
				return false
			}

			override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

				val unprojected = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
				debugText = "touchDown at $screenX, $screenY, b: $button -> unprojected to ${unprojected.x},${unprojected.y}"
				val selectedPlanet: PlanetAnimation? = getSelectedPlanet(unprojected)
				if (selectedPlanet != null) {
					debugText = "touchDown at $screenX, $screenY, b: $button -> unprojected to ${unprojected.x},${unprojected.y}. Clicked on planet ${selectedPlanet.planet.name}"
					println(debugText)
					selectedPlanetAnimation = selectedPlanet
				} else {
					selectedPlanetAnimation = null
				}
				return true
			}

			override fun scrolled(amount: Int): Boolean {
				// wheel up is -1 - I'm zooming out, making everything smaller, getting closer to minZoom (10f)
				// wheel down is 1 - I'm zooming in, making everything smaller, getting closer to maxZoom (1f)
//				println("Camera zoom ${camera.zoom} (max:$maxZoom min:$minZoom) amount: $amount")
				if (amount == 1 && camera.zoom < maxZoom) {
					camera.zoom += 0.2f
				}
				if (amount == -1 && camera.zoom > minZoom) {
					camera.zoom -= 0.2f
				}
				println("camera.zoom ${camera.zoom} (min ${minZoom} max ${maxZoom}")
				return false
			}

		}
	}

	override fun show() {
		// input processor must go in show() or create()
		Gdx.input.inputProcessor = InputMultiplexer(stage, GestureDetector(gestureDetector), inputProcessor);

		// background is on a separate stage
		backgroundStage += hud.backgroundActor
//		stage += hud.sun
		stage += hud.ui
		stage += hud.gameMenu
		stage += hud.blackOverlay
		stage += hud.debug
		stage += hud.planetWindow
		setupHud()
	}

	override fun render(delta: Float) {
		elapsedTime += delta
		camera.update()
		mousePosition.x = Gdx.input.x.toFloat()
		mousePosition.y = Gdx.input.y.toFloat()

		// clear screen
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0.5f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		// background first
		renderBackground()

		// solar system map
		renderPlanets()

		// HUD/UI elements
		renderHUD(delta)

		// debugging tools
		drawGrid()
		debugText()

		// mouse polling
		val mouseOverTarget = getSelectedPlanet(camera.unproject(mousePosition))
		if(mouseOverTarget != null) {
			debugText = mouseOverTarget.planet.baseScience.values.toString()
//			println("Hovering over ${mouseOverTarget.planet.name}")
		}

	}

	private fun renderBackground() {
		backgroundStage.act()
		backgroundStage.draw()
	}

	private fun renderPlanets() {
		systemBatch.projectionMatrix = camera.combined
		systemBatch.use {
			for (p in planets) {
				systemBatch.draw(p.animation.getKeyFrame(elapsedTime, true), p.xPos, (WORLD_HEIGHT / 2f) - (p.spriteHeight / 2f * p.drawScale), 0f, 0f, p.spriteWidth, p.spriteHeight, p.drawScale, p.drawScale, 0f)
			}
		}
	}

	private fun renderHUD(delta: Float) {
		stage.act(delta)
		hud.updateYear(herschel.state.era, herschel.state.year)
		hud.updateScienceIcons(herschel.state.scienceIcons)
		selectedPlanetAnimation?.let { hud.updatePlanetWindow(it)} ?: hud.closeWindow(hud.planetWindow)
		if (debugText.isNotBlank()) hud.updateDebugText(debugText)
		stage.draw()
	}

	override fun dispose() {
		stage.dispose()
		backgroundStage.dispose()
	}

	override fun resize(width: Int, height: Int) {
		systemViewPort.update(width, height)
	}

	private fun setupHud() {
		hud.updateYear(Era.MARS, 2050)
	}

	private fun loadSolarSystem(): SolarSystem = setup.loadSolarSystem()

	private fun loadPlanets(): GdxArray<PlanetAnimation> {
		val planetArray = GdxArray<PlanetAnimation>(true, 9)
		val separator = (WORLD_WIDTH / solarSystem.planets.size).toFloat()
		val furthestPlanetAU = solarSystem.planets.last().distanceFromSun - 10f // -10f for a little padding
		solarSystem.planets.forEachIndexed { i, p ->
			val drawSize = MathUtils.clamp(p.scale / 4f, 0.05f, 1f) + p.scale / 10000
			val clampedPos = MathUtils.clamp(p.distanceFromSun, 0.0f, 2f) - 40f
			val xPosition = (clampedPos + ((i + 1) * p.distanceFromSun / 20))
			val planet = PlanetAnimation(p, (xPosition * separator), drawSize)
			planet.setRectangle((xPosition * separator), (WORLD_HEIGHT / 2f) - (planet.spriteHeight / 2f * planet.drawScale), drawSize, drawSize)
			planetArray.add(planet)
			println("${p.name} xPosition: $xPosition ($clampedPos + ($i * (${p.distanceFromSun} - 40f) [${i * (p.distanceFromSun / 20)}] drawn at $drawSize")
			println("${planet.planet.name} rectangle: ${planet.rectangle}")
		}
		return planetArray
	}

	private fun drawGrid() {
		if (drawGrid) {
			if (systemBatch != null) {
				gridRenderer.projectionMatrix = stage.camera.combined
				systemBatch.begin()
				gridRenderer.begin(ShapeRenderer.ShapeType.Line)
				gridRenderer.color = Color.LIME
				gridRenderer.rect(0f, stage.height / 2f, stage.width, 0.5f)
				gridRenderer.rect(stage.width / 2f, 0f, 0.5f, stage.height)
				gridRenderer.end()
				systemBatch.end()
			}
		}
	}

	private fun getSelectedPlanet(unprojected: Vector3): PlanetAnimation? {
		for (planet in planets) {
			if (unprojected.x >= planet.rectangle.x && unprojected.x <= planet.rectangle.x + planet.rectangle.width) {
				if (unprojected.y >= planet.rectangle.y && unprojected.y <= planet.rectangle.y + planet.rectangle.height) {
					return planet
				}
			}
		}
		return null
	}

	private fun debugText() {
//		systemBatch.begin()
//		font.draw(systemBatch,debugText,camera.position.x, + 400f)
//		systemBatch.end()
	}
}
