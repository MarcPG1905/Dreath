package com.marcpg

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.path
import com.marcpg.env.Environment
import com.marcpg.modules.Module
import com.marcpg.util.SystemInfo
import java.io.File

fun main(args: Array<String>) = CommandArguments().main(args)

class CommandArguments : CliktCommand() {
    // === Startup Flags ===
    val debug by option("-d", "--debug", help = "Whether debugging output should be sent").flag(default = false)
    val noMods by option("-m", "--no-mods", help = "Skip loading any mods").flag(default = false)
    val server by option("-s", "--server", help = "Server to connect to after startup").default("none")

    // === Working Directories ===
    val clientDir by option("-C", "--client-dir", help = "Client's working directory").path().default(File(SystemInfo.prop("user.home") + "/.config/dreath").toPath())
    val gameDir by option("-G", "--game-dir", help = "Game's working directory").path().default(File(SystemInfo.prop("user.dir")).toPath())
    val serverDir by option("-S", "--server-dir", help = "Server's working directory").path().default(File(SystemInfo.prop("user.dir")).toPath())

    // === Game Startup Mode ===
    val environment by option("-e", "--environment", help = "As which environment to run").enum<Environment>(ignoreCase = true).default(Environment.CLIENT)
    val module by option("-M", "--module-test", help = "Specific module to be tested").enum<Module>(ignoreCase = true).default(Module.NONE)

    // === Additional Stuff ===
    val data by argument(help = "Additional data for the game").multiple(required = false)

    override fun run() = Game.run(this)
}
