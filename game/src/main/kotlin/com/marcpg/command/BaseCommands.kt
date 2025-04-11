package com.marcpg.command

import com.marcpg.dreath.command.Command
import com.marcpg.dreath.command.CommandClass
import com.marcpg.dreath.command.CommandManager
import com.marcpg.dreath.command.command
import com.marcpg.mods.ModLoader
import com.marcpg.util.Constants
import com.marcpg.util.SystemInfo
import com.marcpg.util.restartProcess
import com.marcpg.util.truncate
import kotlin.system.exitProcess

class Dreath : CommandClass {
    override fun constructCommand(): Command = command("dreath", listOf(), "Gets information about the dreath game.") {
        action {
            executor.info("""
                Dreath: ${Constants.NAME} ${Constants.VERSION_STRING}
                Protocol: Peg-Protocol ${Constants.PEG_PROTOCOL_VERSION}
                
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
    override fun constructCommand(): Command = command("help", listOf("commands"), "Gets a list of all commands with descriptions.") {
        action {
            for (command in CommandManager.allCommands()) {
                executor.info("/${command.name} (${command.aliases.joinToString(", ")}): ${command.description}")
            }
        }
    }.build()
}

class Stop : CommandClass {
    override fun constructCommand(): Command = command("stop", listOf("end"), "Shuts down the game safely.") {
        action {
            exitProcess(0)
        }
    }.build()
}

class Restart : CommandClass {
    override fun constructCommand(): Command = command("restart", listOf(), "Fully restarts the game and JVM.") {
        action {
            restartProcess()
        }
    }.build()
}

class Mods : CommandClass {
    override fun constructCommand(): Command = command("mods", listOf(), "Manage loaded mods.") {
        subcommand("list") {
            action {
                if (ModLoader.LOADED_MODS.isEmpty()) {
                    executor.info("There are no mods loaded!")
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
                    val modId: String = getArg("mod")
                    val mod = ModLoader.LOADED_MODS[modId]

                    if (mod == null) {
                        executor.info("Could not find mod $modId!")
                        return@action
                    }

                    executor.info("Reloading mod $mod...")
                    ModLoader.reload(mod)
                    executor.info("Done!")
                } else {
                    executor.info("Reloading ${ModLoader.loaded()} loaded mods...")
                    ModLoader.reload()
                    executor.info("Done!")
                }
            }
        }
    }.build()
}
