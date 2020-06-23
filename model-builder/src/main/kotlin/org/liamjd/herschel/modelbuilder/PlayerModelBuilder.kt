package org.liamjd.herschel.modelbuilder

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.liamjd.herschel.models.HQ
import org.liamjd.herschel.models.HQList
import org.liamjd.herschel.models.Player
import org.liamjd.herschel.models.PlayerList
import java.io.File
import java.nio.file.Paths
import java.util.*


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
		hqList.add(HQ(UUID.randomUUID(),"Geneva","Switzerland",1.2f,"Home of the famous CERN particle accelerator, you will benefit from an increased basic science rate of 1.2, as well as a boost to physics research. Material extraction, however, will be only 0.8 of nominal."))
		hqList.add(HQ(UUID.randomUUID(),"Nairobi","Kenya",0.9f, "A true metropolis of Africa, your company prides itself on motivating a diverse workforce to do their very best. Base science and physics is not the strongest, but your understanding of people and the earth make up for it. sci 0.9, psych +, material +, pys -"))
		hqList.add(HQ(UUID.randomUUID(),"Beijing","China",1f, "Even now, the Chinese state looks to the future, a future which may shape the very biology of the Chinese people. bio +"))
		hqList.add(HQ(UUID.randomUUID(),"Dubai","UAE",0.8f, "The end of the oil age forced the Gulf states to find new ways so supply the world with energy, and their understanding of chemistry is unparalleled. sci 0.8, chem ++"))
		hqList.add(HQ(UUID.randomUUID(),"Houston","USA",1.2f, "Manifest destiny finds new forms in this space age, with pure scientific research and engineering skills ahead of most other nations. But religious pressures have kept the USA behind in biology. sci 1.2, eng +, bio --"))
		hqList.add(HQ(UUID.randomUUID(),"Kyoto","Japan",1.4f,"Despite resource-poor chain of islands, nobody doubts the resourcefulness of the Japanese people. sci 1.4, material --, pys -"))
		hqList.add(HQ(UUID.randomUUID(),"Bangalore","India",1f, "Over a billion people can't be wrong; India's ambitions - and need for raw materials - have shot them to the forefront of the space race. mat ++"))
		hqList.add(HQ(UUID.randomUUID(),"São Paulo","Brazil",0.8f, "sci 0.8, mat ++"))

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

	/*	playerList.add(Player(1,"Joseph","Joseph Obuya", "",Gender.MALE,26))
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
*/
		val players = PlayerList(playerList)
		val playerListJson =json.stringify(PlayerList.serializer(),players)

		val playerFile = File(projectDir.absolutePath,"assets/data/players/playerlist.json")
		if(!playerFile.exists()) {
			playerFile.createNewFile()
		}
		playerFile.writeText(playerListJson)
	}
}

