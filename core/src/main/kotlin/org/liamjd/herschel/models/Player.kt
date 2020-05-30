package org.liamjd.herschel.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Player(val id: Int, val firstName: String, val familyName: String, @Transient var playerNickName: String = "", val gender: Gender, var age: Int = 25) {

	// character name
	// character gender
	// city of origin
	// graphical stuff
}


@Serializable
class PlayerList(val players: List<Player>) {

}

@Serializable
enum class Gender(val salutation: String, val pronoun: String) {
	MALE("Mr","he"),
	FEMALE("Ms","she"),
	NONE("","they"),
	NONBINARY("Mx","they")
}

@Serializable
class HQ(val name: String, val country: String)
@Serializable
class HQList(val hqs: List<HQ>)
