package org.liamjd.herschel.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.liamjd.herschel.extensions.UIModel
import java.util.*

@Serializable
class Player(val id: Int, val firstName: String, val familyName: String, val playerNickName: String = "", val gender: Gender, var age: Int = 25, val hq: HQ) {

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
data class HQ(@Transient val id: UUID = UUID.randomUUID(), val name: String, val country: String, val baseScience: Float, var flavourText: String = "") : UIModel

@Serializable
class HQList(val hqs: List<HQ>)
