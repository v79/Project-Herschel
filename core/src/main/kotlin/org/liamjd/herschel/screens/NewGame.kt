package org.liamjd.herschel.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.*
import org.liamjd.herschel.Game
import org.liamjd.herschel.MainMenu
import org.liamjd.herschel.actors.TextureActor
import org.liamjd.herschel.services.newgame.GameSetup

class NewGame(game: Game, stage: Stage, skin: Skin, val setup: GameSetup) : AbstractGameplayScreen(game, stage, skin) {

	override fun show() {
		Scene2DSkin.defaultSkin = screenSkin

		val backgroundActor = TextureActor(Texture(Gdx.files.internal("screens/MainMenu/background.png")))
		stage += backgroundActor

		val title = Label("New Game", screenSkin, "title")
		title.setPosition(stage.width / 2f, stage.height - 20f, Align.center)

		val players = setup.loadPlayerNames()
		val hqs = setup.loadHQList()
		val playerButtonGroup = ButtonGroup<CheckBox>()
		val hqButtonGroup = ButtonGroup<CheckBox>()

		stage += title
		stage.actors {

			textButton("Main Menu") {
				setPosition(10f, (stage.height - 48f))
				onClick {
					hide()
					game.setScreen<MainMenu>()
				}
			}

			table {
				defaults().padBottom(5f).padLeft(5f).align(Align.left)
				setFillParent(true)
				label("Choose your first leader")
				label("Choose the location of your HQ")
				row()

				val playerNameTable = table {
					padRight(5f)
					align(Align.left)
					players.forEach { p ->
						val box = checkBox("${p.firstName} ${p.familyName}", style = "new-game-player-name") { cell ->
							playerButtonGroup.add(this)
							align(Align.left)
							cell.expandX().left()
							cell.expandY().top()
						}
						label(p.age.toString(), style = "new-game-player-name").cell(padRight = 5f)
						label(p.gender.toString(), style = "new-game-player-name").cell(padLeft = 5f).setAlignment(Align.right)
						row()
					}
				}

				val hqTable = table {
					debug()
					align(Align.topLeft).top()
					hqs.forEach { hq ->
						checkBox("${hq.name}: ${hq.country}", style="new-game-player-name") { cell ->
							hqButtonGroup.add(this)
							cell.expandY().left().top()
						}
						row()
					}
				}

				row()

				label("Player nickname", style = "new-game-player-name")
				val nicknameField = textField(style = "new-game-player-name") { }
				textButton("Begin") { cell ->
					onClick {
						println("Nickname entered: " + nicknameField.text)
						stage.clear()
						game.setScreen<InnerPlanets>()
					}
				}

			}
		}
	}
}
