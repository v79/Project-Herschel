package org.liamjd.herschel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.StringBuilder
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.*
import org.liamjd.herschel.Herschel
import org.liamjd.herschel.MainMenu
import org.liamjd.herschel.actors.TextureActor
import org.liamjd.herschel.extensions.radioButton
import org.liamjd.herschel.models.*
import org.liamjd.herschel.services.newgame.GameSetup

class NewGame(herschel: Herschel, stage: Stage, skin: Skin, private val setup: GameSetup) : AbstractGameplayScreen(herschel, stage, skin) {

	override fun show() {
		Scene2DSkin.defaultSkin = screenSkin

		val backgroundActor = TextureActor(Texture(Gdx.files.internal("backgrounds/background-7.png")))
		stage += backgroundActor

		val title = Label("New Game", screenSkin, "title")
		title.setPosition(stage.width / 2f, stage.height - 20f, Align.center)

		// fetch the list of possible HQs from the setup service
		val hqs = setup.loadHQList()
		// button groups are logical groupings, required to create a radio button set
		val hqButtonGroup = ButtonGroup<CheckBox>()

		// if a stage element needs to be referred to before it appears on the stage, create a dummy object first
		var randomNames: Pair<String,String> = setup.getRandomName()
		var startButton: TextButton = TextButton("Start game",screenSkin)

		stage += title
		stage.actors {

			textButton("Main Menu") {
				setPosition(10f, (stage.height - 48f))
				onClick {
					hide()
					herschel.setScreen<MainMenu>()
				}
			}

			var countryDetails: Label = Label("",screenSkin,"new-game-player-name").apply {
				width = 40f
				setWrap(true)
				isVisible = false
			}

			table {
//				debug()
				defaults().align(Align.left).padLeft(10f).padRight(10f).padBottom(10f)
				setFillParent(true)

				label("Where was your company founded?") { cell ->
					cell.colspan(4)
				}

				row().center()

				val hqTable = table {
					hqs.forEachIndexed { index, hq ->
						radioButton(hq,"${hq.name}: ${hq.country}", style="new-game-player-name") { cell ->
							userObject = hq
							cell.colspan(2)
							cell.align(Align.left)
							cell.expandY().left().top()
							hqButtonGroup.add(this)
							onClick {
								with(countryDetails) {
									setText(hq.flavourText)
									isVisible = true
								}
							}
						}
						if((index +1) % 2 == 0) row()
					}
				}
				countryDetails = label("","new-game-player-name") { cell ->
					setText(hqs.first().flavourText)
					setAlignment(Align.topLeft)
					setWrap(true)
					cell.colspan(3)
					cell.fill()
					isVisible = true
				}

				row()

				label("What do your friends call you?") { cell ->
					cell.colspan(4)
				}

				row()

				val rFirstName = label(randomNames.first) { cell ->
					cell.align(Align.right)
				}
				val nicknameField = textField(style="new-game-player-name") { _ ->
					text = "Liam"
					onChange {
						startButton.isDisabled = this.text.isEmpty()
					}
				}
				val rLastName = label(randomNames.second) { cell ->
					cell.expandX()
				}
				button(style = "dice") {
					onClick {
						randomNames = setup.getRandomName()
						rFirstName.setText(randomNames.first)
						rLastName.setText(randomNames.second)
					}
					textTooltip("Randomize")
				}

				row()

				startButton = textButton("Start game") { cell ->
					isDisabled = nicknameField.text.isEmpty()
					cell.colspan(4)
					cell.align(Align.right)
					onClick {
						if(!isDisabled) {
							val selectedHq = hqButtonGroup.allChecked.first().userObject as HQ
							val gameState = initializeGameState(rFirstName.text, rLastName.text, nicknameField.text, selectedHq)
							herschel.state = gameState

							hide()
//							herschel.setScreen<SolSystemTexture>()
							herschel.setScreen<SolarSystemScreen>()
						}
					}
				}
			}
		}
	}

	/**
	 * Initialize the game with the player name and chosen HQ.
	 * For now, gender and age are being hard-coded
	 */
	private fun initializeGameState(firstName: StringBuilder, lastName: StringBuilder, nickName: String, hq: HQ): GameState {
		val gameState = GameState()
		gameState.player = Player(1,firstName.toString(),lastName.toString(), nickName,Gender.MALE,26, hq)
		gameState.year = 2050
		gameState.era = Era.MARS
		return gameState
	}

	override fun render(delta: Float) {
		stage.act(delta)
		super.render(delta)
	}

	override fun dispose() {
		stage.dispose()
		screenSkin.dispose()
	}
}
