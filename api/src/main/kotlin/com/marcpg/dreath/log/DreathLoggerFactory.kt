package com.marcpg.dreath.log

import com.marcpg.dreath.util.MultiOutputStream
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.moveTo
import kotlin.io.path.useLines

/**
 * Represents the factory for creating and managing [DreathLogger]s.
 * @see DreathLogger
 * @author MarcPG
 * @since 0.1.0
 */
object DreathLoggerFactory {
    private val TIME_REGEX = "^\\[(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})".toRegex()

    private lateinit var logFileStream: PrintStream
    private lateinit var main: DreathLogger

    internal val actualSystemOut = System.out
    private val customSystemOut = MultiOutputStream()

    private val loaded: MutableMap<String, DreathLogger> = mutableMapOf()

    /**
     * Initializes this [DreathLogger] factory.
     *
     * **A mod should under no circumstances call This!**
     */
    fun initialize(logFolder: Path) {
        if (this::main.isInitialized)
            throw IllegalStateException("DreathLoggerFactory already initialized")

        val logFile = logFolder.createDirectories().resolve("current.log")

        runCatching {
            if (logFile.exists()) {
                val match = TIME_REGEX.find(logFile.useLines { it.reduce { _, next -> next } })!!
                val day = match.groups[1]!!.value.toInt()
                val month = match.groups[2]!!.value.toInt()
                val year = LocalDateTime.now().year
                val hour = match.groups[3]!!.value
                val minute = match.groups[4]!!.value

                logFile.moveTo(logFile.parent.resolve("old-$year.$month.$day-$hour.$minute.log"))
            }
        }.onFailure {
            Files.deleteIfExists(logFile)
        }

        logFileStream = PrintStream(Files.newOutputStream(logFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC))

        customSystemOut.addOutputStream(logFileStream)
        System.setOut(PrintStream(customSystemOut))

        main = create("Common")
    }

    internal fun create(name: String): DreathLogger {
        return loaded.getOrPut(name) { DreathLogger(name, logFileStream) }
    }
}

/**
 * Gets a dreath logger based on a name.
 *
 * If a logger with this name already exists, it will be returned.
 * If there's no logger yet, it will be created and saved.
 *
 * This should be preferred over directly constructing a [DreathLogger].
 */
fun dreathLogger(name: String): DreathLogger = DreathLoggerFactory.create(name)
