package com.marcpg.render

import com.marcpg.common.EnvironmentClass
import com.marcpg.dreath.log.DreathLogger
import com.marcpg.dreath.log.dreathLogger
import com.marcpg.common.util.Constants
import de.fabmax.kool.KoolApplication
import de.fabmax.kool.KoolConfigJvm
import de.fabmax.kool.math.Vec2i
import de.fabmax.kool.scene.Scene
import kotlin.concurrent.thread
import kotlin.math.roundToInt

fun initRendering(environment: EnvironmentClass) {
    de.fabmax.kool.util.Log.level = de.fabmax.kool.util.Log.Level.OFF
    thread(name = "Rendering Thread") {
        KoolApplication(
            KoolConfigJvm(
                renderBackend = KoolConfigJvm.Backend.VULKAN,
                windowTitle = Constants.VERSION.moduleName,
                windowSize = Vec2i(Constants.DEFAULT_WINDOW_SIZE_X, Constants.DEFAULT_WINDOW_SIZE_Y),
                isVsync = environment.settings.getBoolean("graphical.vsync", true),
                maxFrameRate = environment.settings.getDouble("graphical.fps-limit", 60.0).roundToInt(),
                windowNotFocusedFrameRate = (environment.settings.getDouble("graphical.fps-limit", 60.0) / 2.0).roundToInt(),
                useOpenGlFallback = false,
                showWindowOnStart = true,
            )
        ) {
            Rendering
        }
    }
}

object Rendering : Scene("Dreath Gameplay") {
    val log: DreathLogger = dreathLogger("Rendering")

    init {
        // TODO: Set up scene.
    }
}
