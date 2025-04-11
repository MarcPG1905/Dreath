package com.marcpg.env

import com.marcpg.log.DreathLogger
import java.nio.file.Path
import kotlin.time.TimeSource

abstract class GameEnv {
    abstract val dir: Path
    abstract val log: DreathLogger

    abstract fun run(start: TimeSource.Monotonic.ValueTimeMark)
    abstract fun end()
}