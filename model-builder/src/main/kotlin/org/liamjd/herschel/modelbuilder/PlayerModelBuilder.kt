package org.liamjd.herschel.modelbuilder

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.liamjd.herschel.models.*
import java.io.File
import java.nio.file.Paths




object PlayerModelBuilder {

	private val json = Json(JsonConfiguration(prettyPrint = true))
	private val projectDir: File = Paths.get(".").toAbsolutePath().normalize().toFile()

	@JvmStatic
	fun main(args: Array<String>) {

		buildPlayerList()
		buildHQList()

	}

	fun buildHQList() {
		val hqList = mutableListOf<HQ>()
		hqList.add(HQ("Geneva","Switzerland",1.2f,"Home of the famous CERN particle accelerator, you will benefit from an increased basic science rate of 1.2, as well as a boost to physics research. Material extraction, however, will be only 0.8 of nominal."))
		hqList.add(HQ("Nairobi","Kenya",0.9f, "sci 0.9, psych +, material +, pys -"))
		hqList.add(HQ("Beijing","China",1f, "bio +"))
		hqList.add(HQ("Dubai","UAE",0.8f, "sci 0.8, chem ++"))
		hqList.add(HQ("Houston","USA",1.2f, "sci 1.2, eng +, bio --"))
		hqList.add(HQ("Kyoto","Japan",1.4f,"sci 1.4, material --, pys -"))
		hqList.add(HQ("Bangalore","India",1f, "mat ++"))
		hqList.add(HQ("São Paulo","Brazil",0.8f, "sci 0.8, mat +"))

		val hqs = HQList(hqList)
		val hqListJson = json.stringify(HQList.serializer(),hqs)
		val hqFile = File(projectDir.absolutePath,"assets/data/players/hqlist.json")
		if(!hqFile.exists()) {
			hqFile.createNewFile()
		}
		hqFile.writeText(hqListJson)
	}

	fun buildPlayerList() {
		val playerList = mutableListOf<Player>()

		playerList.add(Player(1,"Joseph","Joseph Obuya", "",Gender.MALE,26))
		playerList.add(Player(2,"Chima","Kwesi Obama", "", Gender.MALE,27))
		playerList.add(Player(3,"Marjani","Onyekachukwu Maina", "", Gender.FEMALE,30))
		playerList.add(Player(4,"Amahle","Inyene Okafor", "", Gender.FEMALE,31))
		playerList.add(Player(5,"Eugène","Caron", "", Gender.MALE,24))
		playerList.add(Player(6,"Rachel","Soraya Masson", "", Gender.FEMALE,24))
		playerList.add(Player(7,"Issac","Dominick Rattray", "", Gender.MALE,45))
		playerList.add(Player(8,"Meredith","McFarland", "", Gender.FEMALE,32))
		playerList.add(Player(9,"Erika"," Franklyn","", Gender.FEMALE,29))
		playerList.add(Player(10,"Peter","Hendry", "", Gender.MALE,27))
		playerList.add(Player(11,"Youssef","Alfarsi", "", Gender.MALE,26))
		playerList.add(Player(12,"Karam","Ajam", "", Gender.FEMALE,40))
		playerList.add(Player(13,"Zhong","Lin", "", Gender.FEMALE,32))
		playerList.add(Player(14,"Bai","Yang", "", Gender.MALE,33))
		playerList.add(Player(15,"Jia","Chen", "", Gender.MALE,35))

		val players = PlayerList(playerList)
		val playerListJson =json.stringify(PlayerList.serializer(),players)

		val playerFile = File(projectDir.absolutePath,"assets/data/players/playerlist.json")
		if(!playerFile.exists()) {
			playerFile.createNewFile()
		}
		playerFile.writeText(playerListJson)
	}
}

