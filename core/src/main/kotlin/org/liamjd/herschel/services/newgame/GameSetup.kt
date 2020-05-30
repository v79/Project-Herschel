package org.liamjd.herschel.services.newgame

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.liamjd.herschel.models.HQ
import org.liamjd.herschel.models.HQList
import org.liamjd.herschel.models.Player
import org.liamjd.herschel.models.PlayerList
import java.io.File
import kotlin.random.Random

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

	fun getRandomName(): Pair<String,String> {
		val firstNameString = """
			Bridgett  
			Delois  
			Martha  
			Jimmie  
			Jefferson  
			Viviana  
			Madeleine  
			Evelyne  
			King  
			Wilton  
			Joel  
			Claire  
			Zelma  
			Vernon  
			Casey  
			Ardelle  
			Kitty  
			Phyliss  
			Kimberlie  
			Madge  
			Bruce  
			Kelvin  
			Dee  
			Sherice  
			Peggie  
			Devon  
			Levi  
			Pansy  
			Willene  
			Heath  
			Dennise  
			Ashlee  
			Marianela  
			Gino  
			Brenton  
			Lynne  
			Jeri  
			Denita  
			Barbie  
			Aimee  
			Toshiko  
			Michele  
			Heather  
			Natalie  
			Apryl  
			Keneth  
			Andreas  
			Caridad  
			Malvina  
			Rueben  
		""".trimIndent()

		val lastNameString = """
			Overstreet		
			Marks			
			Hammersmith		
			Dever			
			Corzine			
			Decelles		
			Humiston		
			Gautreau		
			Knuckles		
			Freund			
			Asaro			
			Mcdaniel		
			Seman			
			Crusoe			
			Greenlee		
			Stella			
			Hoying			
			Catoe			
			Ofarrell		
			Sanor			
			Blane			
			Schoch			
			Smoak			
			Huie			
			Pair			
			Cortright		
			Kulig			
			Marland			
			Marinez			
			Machin			
			Wilsey			
			Veasley			
			Morabito		
			Maslowski		
			Aceuedo			
			Faria			
			Rawling			
			Laforce			
			Belknap			
			Caufield		
			Stillwagon		
			Jacobus			
			Coffield		
			Greenbaum		
			Barrus			
			Mclelland		
			Zink			
			Lefkowitz		
			Fahie			
			Collier			
		""".trimIndent()

		val firstNames = firstNameString.split("\n")
		val lastNames = lastNameString.split("\n")

		val r = Random(System.currentTimeMillis())
		return Pair(firstNames.get(r.nextInt(0,firstNames.size-1)),lastNames.get(r.nextInt(0,lastNames.size-1)))
	}
}
