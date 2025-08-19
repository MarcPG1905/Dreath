package common

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.path
import common.modules.Module
import common.util.SystemInfo
import java.io.File

fun main(args: Array<String>) {
    Environment.current().commandArguments()!!.main(args)
}

abstract class CommandArguments : CliktCommand(name = "dreath") {
    // === Startup Flags ===
    val debug by option("-d", "--debug", help = "Whether debugging output should be sent").flag(default = false)
    val noMods by option("-m", "--no-mods", help = "Skip loading any mods").flag(default = false)

    // === Working Directories ===
    val gameDir by option("-G", "--game-dir", help = "Game's working directory").path().default(File(SystemInfo.prop("user.dir")).toPath())

    // === Game Startup Mode ===
    val module by option("-M", "--module-test", help = "Specific module to be tested").enum<Module>(ignoreCase = true).default(Module.NONE)

    // === Additional Stuff ===
    val data by argument(help = "Additional data for the game").multiple(required = false)

    override fun run() = Game.run(this)
}
