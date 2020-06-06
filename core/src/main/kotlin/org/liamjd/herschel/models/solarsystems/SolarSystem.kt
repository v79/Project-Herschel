package org.liamjd.herschel.models.solarsystems

import kotlinx.serialization.Serializable
import org.liamjd.herschel.models.science.Science

typealias HexColor = Int

@Serializable
class SolarSystem(val star: Star, val isBinary: Boolean, val planets: List<Planet>, val asteroidBeltPosition: Int) {
	val numPlanets: Int
		get() = planets.filter { planet -> !planet.isDwarf }.size

}

@Serializable
class Planet(val name: String, val planetClass: PlanetClass, val scale: Float, val color: HexColor, val isDwarf: Boolean, val rings: Int, val moons: Int, var textureAtlas: String? = "") {

	val baseScience: MutableMap<Science,Float> = mutableMapOf()
	val modifiers: MutableMap<String,Attribute> = mutableMapOf()
}

@Serializable
class Star(val name: String, val starClass: StarClass, val color: HexColor) {
}

/**
 * Stellar class - Radius - Mass - Luminosity - Temp - Example
	O6 			18 		40 	500,000 	38,000 	Theta1 Orionis C
	B0 			7.4 	18 	20,000 		30,000 	Phi1 Orionis
	B5 			3.8 	6.5 	800 	16,400 	Pi Andromedae A
	A0 			2.5 	3.2 	80 		10,800 	Alpha Coronae Borealis A
	A5 			1.7 	2.1 	20 		8,620 	Beta Pictoris
	F0 			1.3 	1.7 	6 		7,240 	Gamma Virginis
	F5 			1.2 	1.3 	2.5 	6,540 	Eta Arietis
	G0 			1.05 	1.10 	1.26 	5,920 	Beta Comae Berenices
	G2 			1 		1 		1 		5,780 	Sun[note 2]
	G5 			0.93 	0.93 	0.79 	5,610 	Alpha Mensae
	K0 			0.85 	0.78 	0.40 	5,240 	70 Ophiuchi A
	K5 			0.74 	0.69 	0.16 	4,410 	61 Cygni A[27]
	M0 			0.51 	0.60 	0.072 	3,800 	Lacaille 8760
	M5 			0.32 	0.21 	0.0079 	3,120 	EZ Aquarii A
	M8 			0.13 	0.10 	0.0008 	2,660 	Van Biesbroeck's star[28]
 */
enum class StarClass(val sizeRange: IntRange) {
	YELLOW_DWARF(80..120),
	RED_DWARF(15..30),
	WHITE_DWARF(10..20),
	BROWN_DWARF(8..15),
	RED_GIANT(10000..100000),
	BLUE_GIANT(8000..14000),
	SUPERGIANT(90000..120000),
	NEUTRON(1..1),
	PULSAR(1..1),
	BLACK_HOLE(99999999..999999999)
}

enum class PlanetClass(name: String) {
	GAS_GIANT("Gas Giant"),
	ICE_GIANT("Ice Giant"),
	TERRA("Earth-like"),
	ROCKY("Rocky"),
	TOXIC("Toxic")
}

@Serializable
sealed class Attribute {
	@Serializable
	data class fValue(val value: Float) : Attribute() {
		override fun toString(): String {
			return value.toString()
		}
	}
	@Serializable
	data class iValue(val value: Int): Attribute()	{
		override fun toString(): String {
			return value.toString()
		}
	}
	@Serializable
	data class sValue(val value: String): Attribute() {
		override fun toString(): String {
			return value.toString()
		}
	}
	@Serializable
	data class bValue(val value: Boolean): Attribute() {
		override fun toString(): String {
			return value.toString()
		}
	}
	@Serializable
	data class lValue(val value: Long) : Attribute() {
		override fun toString(): String {
			return value.toString()
		}
	}
}

@Serializable
class SolarSystemList(val solarsystem: List<SolarSystem>)
