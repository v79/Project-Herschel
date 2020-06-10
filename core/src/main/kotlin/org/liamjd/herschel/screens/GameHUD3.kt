package org.liamjd.herschel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.scene2d.*
import org.liamjd.herschel.Herschel
import org.liamjd.herschel.MainMenu
import org.liamjd.herschel.V_HEIGHT_PIXELS
import org.liamjd.herschel.V_WIDTH_PIXELS
import org.liamjd.herschel.actors.TextureActor
import org.liamjd.herschel.models.Era
import org.liamjd.herschel.services.UIConfiguration
import org.liamjd.herschel.uicomponents.ScienceIcon
import org.liamjd.herschel.uicomponents.scienceIcon

class GameHUD3(val herschel: Herschel, val screenSkin: Skin) {

	val backgroundActor = TextureActor(Texture(Gdx.files.internal("backgrounds/background-7.png")))
	val sun: Image
	init {
		Scene2DSkin.defaultSkin = screenSkin
		sun = scene2d.image("sun")
		sun.setPosition(-50f,0f)
	}


	val gameMenu = buildGameMenu()


	private val scienceIcons =  UIConfiguration.scienceIcons
	private val scienceIconMap = mutableMapOf<String,KImageTextButton>()
	private val scienceIconGroup = scene2d.horizontalGroup { cell ->
		space(3f)
		scienceIcons.forEach { science ->

			scienceIconMap.putIfAbsent(science.name,scienceIcon(science) {
				scaleIcon(image, science.iconScale)
				val tt = textTooltip(science.tooltip, style = "simple")
				onClick {
					println("Clicked on ${science.name} with value ${science.value}")
					science.value++
					this.label.setText(science.value.toString())
					tt.actor.setText("${science.name} now at ${science.value}")
				}
			})
		}
	}

	private val mainMenuButton = scene2d.textButton("Main menu") {
		onClick {
			gameMenu.pack()
			gameMenu.zIndex = stage.actors.size // this is relative, not absolute
			gameMenu.setPosition(V_WIDTH_PIXELS / 2f - (gameMenu.width / 2f), V_HEIGHT_PIXELS / 2f - (gameMenu.height / 2f))
			gameMenu.isVisible = true
		}
	}
	private val yearLabel = scene2d.label("year placeholder") { cell ->
		setAlignment(Align.right)
	}

	private val topRow = scene2d.table {
		background = screenSkin.getDrawable("ui-background-blue")
		top().left()
//		debug()
		add(mainMenuButton).left()
		add(scienceIconGroup)
		add(yearLabel).padRight(10f).growX().right()
	}

	private val nextTurnButton = scene2d.textButton("End turn") {
		onClick {
			herschel.state.endTurn()
		}
	}

	val ui = scene2d.table(screenSkin) {
		Scene2DSkin.defaultSkin = screenSkin
		setFillParent(true)
		top().left()
		add(topRow).growX()
		row().expandY().bottom().right()
		add(nextTurnButton)
	}

	private fun KImageTextButton.scaleIcon(image: Image, scale: Float, align: Int = Align.center) {
		image.scaleBy(scale) // scale first
		pack()    // then pack
		image.setOrigin(align) // and finally center. The order matters!
	}

	private fun buildGameMenu(): KDialog {
		return scene2d.dialog("Game Menu", style = "blue") {
			isMovable = false
			isVisible = false
			verticalGroup {
				textButton("Continue") {
					onClick {
//						hideBlackOverlay()
						this@dialog.isVisible = false
					}
				}
				textButton("Save")
				textButton("Options")
				textButton("Return to main menu") {
					onClick {
//						this@InnerPlanets.hide()
						herschel.setScreen<MainMenu>()
					}
				}
			}.space(10f)
		}
	}

	fun updateYear(era: Era, year: Int) {
		yearLabel.setText("${era} Year ${year}")
	}

	fun updateScienceIcons(updatedScienceIcons: Array<ScienceIcon>) {
		updatedScienceIcons.forEach { s ->
			scienceIconMap[s.name]?.label?.setText(s.value.toString())
		}
	}

}
