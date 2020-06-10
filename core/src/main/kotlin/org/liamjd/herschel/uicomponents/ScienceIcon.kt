package org.liamjd.herschel.uicomponents

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class ScienceIcon(val name: String, val icon: String) {
	var value: Int = 123
	var tooltip: String = name
	var iconScale = -0.5f
}


@Scene2dDsl
@OptIn(ExperimentalContracts::class)
inline fun <S> KWidget<S>.scienceIcon(
		obj: ScienceIcon,
		style: String = obj.icon,
		skin: Skin = Scene2DSkin.defaultSkin,
		init: KImageTextButton.(S) -> Unit = {}
): KImageTextButton {
	contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
	return  actor(KImageTextButton(obj.value.toString(), skin, style), init)
}

