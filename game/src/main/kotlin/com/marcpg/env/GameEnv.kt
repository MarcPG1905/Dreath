package com.marcpg.env

import com.marcpg.log.DreathLogger
import com.marcpg.util.config.Settings
import java.nio.file.Path
import kotlin.time.TimeSource

abstract class GameEnv {
    abstract val dir: Path
    abstract val log: DreathLogger
    abstract val settings: Settings

    abstract fun run(start: TimeSource.Monotonic.ValueTimeMark)
    open fun end() {
        settings.save()
    }
}