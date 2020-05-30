package org.liamjd.herschel.services.newgame

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.liamjd.herschel.models.HQ
import org.liamjd.herschel.models.HQList
import org.liamjd.herschel.models.Player
import org.liamjd.herschel.models.PlayerList
import java.io.File

class GameSetup {

	fun loadPlayerNames(): List<Player> {
		val PLAYERLIST_JSON = "assets/data/players/playerlist.json"

		val json = Json(JsonConfiguration.Stable)
		val playerListFile = File(PLAYERLIST_JSON)
		val playersFromJson = json.parse(PlayerList.serializer(),playerListFile.readText())
		return playersFromJson.players
	}

	fun loadHQList(): List<HQ> {
		val HQLIST_JSON = "assets/data/players/hqlist.json"
		val json = Json(JsonConfiguration.Stable)
		val hqListFile = File(HQLIST_JSON)
		val hqsFromJson = json.parse(HQList.serializer(),hqListFile.readText())
		return hqsFromJson.hqs
	}
}
