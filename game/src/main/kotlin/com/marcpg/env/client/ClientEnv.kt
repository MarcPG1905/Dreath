package com.marcpg.env.client

import com.marcpg.Game
import com.marcpg.env.GameEnv
import com.marcpg.log.DreathLogger
import com.marcpg.log.dreathLogger
import com.marcpg.util.Constants
import com.marcpg.util.DiscordRichPresence
import com.marcpg.visual.Rendering
import de.fabmax.kool.KoolApplication
import de.fabmax.kool.KoolConfigJvm
import de.fabmax.kool.math.Vec2i
import java.nio.file.Path
import kotlin.concurrent.thread
import kotlin.io.path.createDirectories
import kotlin.time.TimeSource

object ClientEnv : GameEnv() {
    override val dir: Path = Game.CLI_ARGS.clientDir.createDirectories()
    override val log: DreathLogger = dreathLogger("Client")

    override fun run(start: TimeSource.Monotonic.ValueTimeMark) {
        log.fine("Starting client logic ${start.elapsedNow()} into startup.")

        // Starting rendering init and loop.
        log.info("Initializing kool logic for client...")
        de.fabmax.kool.util.Log.level = de.fabmax.kool.util.Log.Level.OFF
        thread(name = "Rendering Thread") {
            KoolApplication(KoolConfigJvm(
                renderBackend = KoolConfigJvm.Backend.VULKAN,
                windowTitle = Constants.NAME,
                windowSize = Vec2i(Constants.DEFAULT_WINDOW_SIZE_X, Constants.DEFAULT_WINDOW_SIZE_Y),
                useOpenGlFallback = false,
                showWindowOnStart = true,
            )) {
                Rendering()
            }
        }
        log.fine("Initialized kool logic ${start.elapsedNow()} into startup.")

        DiscordRichPresence.run()
    }

    override fun end() {
    }
}