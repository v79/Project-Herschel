package org.liamjd.herschel.modelbuilder

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.liamjd.herschel.models.science.Science
import org.liamjd.herschel.models.solarsystems.*
import java.io.File
import java.nio.file.Paths
import kotlin.random.Random


object SolSystemBuilder {
	private val json = Json(JsonConfiguration(prettyPrint = true))
	private val projectDir: File = Paths.get(".").toAbsolutePath().normalize().toFile()

	private val r = Random(System.currentTimeMillis())

	@JvmStatic
	fun main(args: Array<String>) {
		buildOurSolarSystem()
	}

	private fun buildOurSolarSystem() {

		val solPlanets = ArrayList<Planet>()
		val mercury = Planet("Mercury",PlanetClass.ROCKY,0.38f,0xeeeeee, isDwarf = false, rings = 0, moons = 0).apply {
			textureAtlas = "planets/mercury.atlas"
		}

		val venus = Planet("Venus",PlanetClass.TOXIC,0.95f, 0xFFB200, isDwarf = false, rings = 0, moons = 0).apply {
			textureAtlas = "planets/venus.atlas"
		}
		val earth = Planet("Earth",PlanetClass.TERRA,1f, 0x057DFF, isDwarf = false, rings = 0, moons = 1).apply {
			textureAtlas = "planets/earth.atlas"
		}
		val mars = Planet("Mars",PlanetClass.TERRA,0.53f, 0xFF4D21, isDwarf = false, rings = 0, moons = 2).apply {
			textureAtlas = "planets/mars.atlas"
		}
		val jupiter = Planet("Jupiter",PlanetClass.GAS_GIANT,1200f,0xFFD2BF,false,rings = 1, moons = 79).apply {
			textureAtlas = "planets/jupiter.atlas"
		}
		val saturn = Planet("Saturn",PlanetClass.GAS_GIANT,945f,0xFFF282,isDwarf = false,rings = 4,moons = 82).apply {
			textureAtlas = "planets/saturn.atlas"
		}
		val uranus = Planet("Uranus",PlanetClass.ICE_GIANT,400f,0xAFFCFF,false, rings = 0, moons = 27).apply {
			textureAtlas = "planets/uranus.atlas"
		}
		val neptune = Planet("Neptune",PlanetClass.ICE_GIANT,388f,0x60A8FF,isDwarf = false,rings = 1,moons = 14).apply {
			textureAtlas = "planets/neptune.atlas"
		}
		val pluto = Planet("Pluto",PlanetClass.ROCKY,0.16f,0xFBF9FF,isDwarf = true,rings = 0, moons = 1).apply {
			textureAtlas = "planets/pluto.atlas"
		}
		solPlanets.addAll(arrayOf(mercury,venus,earth,mars,jupiter,saturn,uranus,neptune,pluto))
		solPlanets.forEach { p ->
			Science.values().forEach { s ->
				p.baseScience[s] = r.nextInt(0,3).toFloat()
			}
		}
		earth.modifiers["Population"] = Attribute.lValue(7000000000)
		mercury.modifiers["Tidally locked"] = Attribute.bValue(true)

		val sol = SolarSystem(Star("Sol", StarClass.YELLOW_DWARF, 0xffff00), false, solPlanets, 3)

		val solList = SolarSystemList(listOf(sol))
		val solListString = json.stringify(SolarSystemList.serializer(),solList)
		val solListFile = File(projectDir.absolutePath,"assets/data/solarsystems/sol.json")
		if(!solListFile.exists()) {
			solListFile.createNewFile()
		}
		solListFile.writeText(solListString)

		println(sol.numPlanets)
	}

}
