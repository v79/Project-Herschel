package org.liamjd.game.desktop

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import org.liamjd.herschel.Herschel

object DesktopLauncher {
	@JvmStatic
	fun main(args: Array<String>) {
		createApplication()
	}

	private fun createApplication(): LwjglApplication? {
		return LwjglApplication(Herschel(), getDefaultConfiguration())
	}

	private fun getDefaultConfiguration(): LwjglApplicationConfiguration? {
		val configuration = LwjglApplicationConfiguration()
		configuration.title = "Herschel"
		configuration.width = 1440
		configuration.height = 900
		for (size in intArrayOf(128, 64, 32, 16)) {
			configuration.addIcon("libgdx$size.png", FileType.Internal)
		}
		return configuration
	}
}
