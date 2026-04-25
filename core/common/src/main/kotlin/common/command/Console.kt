package common.command

import com.marcpg.dreath.command.CommandExecutor
import com.marcpg.dreath.log.DreathLoggerFactory
import com.marcpg.dreath.util.vector.Location
import common.Game
import common.command.console.ConsoleCompleter
import common.command.console.LineReaderPrintStream
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.history.DefaultHistory
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.utils.InfoCmp
import java.util.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class Console {
    private var isRunning = true
    private val consoleThread: Thread

    private var terminal: Terminal? = null

    init {
        isRunning = true
        consoleThread = thread(name = "Console Thread", isDaemon = true) {
            runConsole()
        }
    }

    private fun runConsole() {
        terminal = TerminalBuilder.builder()
            .name("Dreath Server")
            .system(true)
            .ffm(true)
            .build()

        val lineReader = LineReaderBuilder.builder()
            .terminal(terminal)

            .history(DefaultHistory())
            .variable(LineReader.HISTORY_FILE, Game.dir.resolve(".histfile"))
            .variable(LineReader.HISTORY_SIZE, 100)

            .completer(ConsoleCompleter())
            .variable(LineReader.MENU_COMPLETE, true)

            // .parser()
            // .highlighter()
            // .expander()
            .build()

        DreathLoggerFactory.setActualSystemOut(LineReaderPrintStream(lineReader))

        while (isRunning) {
            try {
                val line = lineReader.readLine("~# ")
                if (!line.isNullOrBlank()) {
                    val args = line.split("\\s|=".toRegex())
                    runCatching {
                        CommandParser.parse(ConsoleCommandExecutor, args)
                    }.onFailure { e ->
                        ConsoleCommandExecutor.error("Command execution failed: ${e.message}")
                    }
                }
            } catch (e: UserInterruptException) {
                exitProcess(0)
            } catch (e: EndOfFileException) {
                exitProcess(0)
            } catch (e: Exception) {
                exitProcess(1) // Not sure if setting this to 1 even does anything.
            }
        }
    }

    fun clear() {
        terminal?.puts(InfoCmp.Capability.clear_screen)
        terminal?.flush()
    }

    fun stop() {
        isRunning = false
        consoleThread.interrupt()
    }

    object ConsoleCommandExecutor : CommandExecutor {
        override fun location(): Location = Location.DUMMY
        override fun name(): String = "Console"
        override fun locale(): Locale = Locale.getDefault()

        override fun fine(raw: String) = raw.split("\n").forEach { Game.log.fine(it) }
        override fun config(raw: String) = raw.split("\n").forEach { Game.log.config(it) }
        override fun success(raw: String) = raw.split("\n").forEach { Game.log.success(it) }

        override fun info(raw: String) = raw.split("\n").forEach { Game.log.info(it) }
        override fun warn(raw: String) = raw.split("\n").forEach { Game.log.warn(it) }
        override fun error(raw: String) = raw.split("\n").forEach { Game.log.error(it) }
    }
}
