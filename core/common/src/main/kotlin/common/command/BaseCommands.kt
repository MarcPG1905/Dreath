package common.command

import com.marcpg.dreath.command.Command
import com.marcpg.dreath.command.CommandClass
import com.marcpg.dreath.command.command
import com.marcpg.dreath.util.Constants
import com.marcpg.libpg.util.truncate
import common.Game
import common.mods.ModLoader
import common.util.SystemInfo
import kotlin.system.exitProcess

class Dreath : CommandClass {
    override fun constructCommand(): Command = command(name = "dreath", description = "Gets information about the dreath game.") {
        action {
            executor.info("""
                Dreath: ${Constants.VERSION}
                Protocol: ${Constants.PROTOCOL_VERSION}
                
                Loaded Mods: ${ModLoader.loaded()}
                
                Server-Info:
                    JVM: ${SystemInfo.jvm()}
                    System: ${SystemInfo.os()}
                    User: ${SystemInfo.user()}
            """.trimIndent())
        }
    }.build()
}

class Help : CommandClass {
    override fun constructCommand(): Command = command(name = "help", description = "Gets a list of all commands with descriptions.") {
        action {
            for (command in CommandRegistrar.allCommands()) {
                executor.info("${command.name} (${command.aliases.joinToString(", ")}): ${command.description}")
            }
        }
    }.build()
}

class Clear : CommandClass {
    override fun constructCommand(): Command = command(name = "clear", description = "Clears the console.") {
        action {
            if (Game.CONSOLE == null) {
                executor.error("There is no console running right now.")
            } else {
                Game.CONSOLE!!.clear()
            }
        }
    }.build()
}

class Stop : CommandClass {
    override fun constructCommand(): Command = command(name = "stop", aliases = listOf("exit"), description = "Shuts down the game safely.") {
        action {
            exitProcess(0)
        }
    }.build()
}

class Mods : CommandClass {
    override fun constructCommand(): Command = command(name = "mods", description = "Manage loaded mods.") {
        subcommand("list") {
            action {
                if (ModLoader.LOADED_MODS.isEmpty()) {
                    executor.info("There are no mods loaded.")
                    return@action
                }

                executor.info("Showing ${ModLoader.loaded()} loaded mods:")
                for (mod in ModLoader.LOADED_MODS.values) {
                    executor.info("""
                        ${mod.info.name}:
                            ID: ${mod.info.id}
                            Version: ${mod.info.version}
                            Description: ${mod.info.description.truncate()}
                    """.trimIndent())
                }
            }
        }
        subcommand("reload") {
            option("mod", 'm', { it }, { it in ModLoader.LOADED_MODS }, "Specific mod to reload, instead of all.")

            action {
                if (hasArg("mod")) {
                    executor.info("Mod arg")
                    val modId: String = getArg("mod")
                    val mod = ModLoader.LOADED_MODS[modId]

                    if (mod == null) {
                        executor.info("Could not find mod $modId!")
                        return@action
                    }

                    executor.info("Reloading mod $mod...")
                    ModLoader.reloadMod(mod)
                } else {
                    executor.fine("No  mod argument")
                    executor.info("Reloading ${ModLoader.loaded()} loaded mods...")
                    ModLoader.reload()
                }
                executor.success("Done!")
            }
        }
    }.build()
}
