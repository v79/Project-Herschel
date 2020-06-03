package org.liamjd.herschel.services

import ktx.collections.GdxArray
import org.liamjd.herschel.uicomponents.ScienceIcon

object UIConfiguration {

	val scienceIcons: GdxArray<ScienceIcon> = GdxArray()

	init {
		scienceIcons.add(ScienceIcon("Biology","science_icon_biology"))
		scienceIcons.add(ScienceIcon("Chemistry","science_icon_chemistry"))
		scienceIcons.add(ScienceIcon("Engineering","science_icon_engineering"))
		scienceIcons.add(ScienceIcon("Physics","science_icon_physics"))
		scienceIcons.add(ScienceIcon("Psychology","science_icon_psychology"))

	}
}
