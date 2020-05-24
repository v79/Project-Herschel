package org.liamjd.game.v2.desktop

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import org.liamjd.game.v2.Version2
import org.liamjd.game.v3.Version3

object DesktopLauncher {
	@JvmStatic
	fun main(args: Array<String>) {
		createApplication()
	}

	private fun createApplication(): LwjglApplication? {
		return LwjglApplication(Version3(), getDefaultConfiguration())
	}

	private fun getDefaultConfiguration(): LwjglApplicationConfiguration? {
		val configuration = LwjglApplicationConfiguration()
		configuration.title = "Herschel"
		configuration.width = 640
		configuration.height = 480
		for (size in intArrayOf(128, 64, 32, 16)) {
			configuration.addIcon("libgdx$size.png", FileType.Internal)
		}
		return configuration
	}
}
