package com.marcpg.command

import com.marcpg.Game
import com.marcpg.dreath.command.CommandExecutor
import com.marcpg.dreath.util.Location
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.concurrent.thread

class Console {
    private var isRunning = true
    private val consoleThread: Thread

    private val commandExecutor = ConsoleCommandExecutor()

    init {
        isRunning = true
        consoleThread = thread(name = "Console Thread", isDaemon = true) { runConsole() }
    }

    private fun runConsole() {
        val reader = BufferedReader(InputStreamReader(System.`in`))
        while (isRunning) {
            print("~# ")
            val line = reader.readLine()
            if (line != null) {
                val args = line.split("\\s".toRegex())
                runCatching {
                    CommandParser.parse(commandExecutor, args)
                }.onFailure { e ->
                    commandExecutor.error("Command execution failed: ${e.message}")
                }
            }
        }
    }

    fun stop() {
        isRunning = false
        consoleThread.interrupt()
    }

    class ConsoleCommandExecutor : CommandExecutor {
        override fun location(): Location = Location.DUMMY
        override fun name(): String = "Console"
        override fun locale(): Locale = Locale.getDefault()

        override fun info(raw: String) = raw.split("\n").forEach { Game.LOG.info(it) }
        override fun warn(raw: String) = raw.split("\n").forEach { Game.LOG.warn(it) }
        override fun error(raw: String) = raw.split("\n").forEach { Game.LOG.error(it) }
    }
}