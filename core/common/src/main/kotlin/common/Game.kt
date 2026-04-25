package common

import com.marcpg.dreath.Dreath
import com.marcpg.dreath.log.DreathLogger
import com.marcpg.dreath.log.DreathLoggerFactory
import com.marcpg.dreath.log.dreathLogger
import com.marcpg.dreath.util.registry.RegistrarType
import com.marcpg.dreath.util.registry.Registration
import com.marcpg.libpg.color.Ansi
import com.marcpg.libpg.util.toTitleCase
import common.channels.*
import common.command.Console
import common.mods.ModLoader
import common.modules.Module
import common.modules.ModuleTesting
import common.registrars.ChannelRegistrar
import common.util.SystemInfo
import common.util.config.CommonConfig
import common.util.elapsedMs
import common.util.io.FilteredOutputStream
import common.util.keepAliveProcess
import common.util.onShutdownProcess
import protocol.channel.ChannelManager
import protocol.socket.SocketManager
import java.nio.file.Files
import java.nio.file.Path
import kotlin.time.TimeSource

object Game {
    val MAIN_THREAD: Thread = Thread.currentThread()
    val ENVIRONMENT: Environment = Environment.current()
    lateinit var CLI_ARGS: CommandArguments

    lateinit var DIR: Path
    lateinit var LOG: DreathLogger
    internal var CONSOLE: Console? = null

    var running: Boolean = true
        internal set

    /**
     * Called when the game starts. Runs the main logic of the game.
     * @param args The command line arguments used.
     */
    fun run(args: CommandArguments) {
        val start = TimeSource.Monotonic.markNow()

        CLI_ARGS = args
        DIR = Files.createDirectories(CLI_ARGS.gameDir)

        // Prevents errors which aren't from the DreathLogger from being printed.
        System.setErr(FilteredOutputStream(System.err))

        Dreath.initialize({ running }, { CLI_ARGS.debug })

        CommonConfig.init()

        DreathLoggerFactory.initialize(CLI_ARGS.gameDir.resolve("logs"))
        DreathLogger.logLevel = CommonConfig.loggerLevel
        DreathLogger.ansiFormatting = CommonConfig.loggerAnsiFormatting
        LOG = dreathLogger("Common")

        LOG.config("Debug: ${if (Dreath.isDebug) "Enabled" else "Disabled"}")
        LOG.config("JVM: ${SystemInfo.jvm()}")
        LOG.config("System: ${SystemInfo.os()}")
        LOG.config("User: ${SystemInfo.user()}")
        LOG.config("Environment: ${ENVIRONMENT.name.toTitleCase()}")

        if (CLI_ARGS.module != Module.NONE) {
            ModuleTesting.run(start)
            return
        }

        CONSOLE = Console()

        SocketManager.open(ENVIRONMENT == Environment.SERVER)
        ChannelManager.initialize(ChannelRegistrar)

        onShutdownProcess { end() }

        LOG.fine("Starting Dreath main logic...")

        val loadMods = !CLI_ARGS.noMods && CommonConfig.loadMods

        if (loadMods) {
            LOG.fine("Loading mods...")
            val loaded = ModLoader.init()
            if (loaded.first > 0) {
                LOG.success("Found and initialized ${loaded.first} mods ${start.elapsedMs()} into startup.")
            } else {
                LOG.info("No mods found.")
            }
            LOG.info("Total mods loaded: ${loaded.second}")
        } else {
            LOG.config("Skipping mod loading due to flag or configuration.")
        }

        ENVIRONMENT.instance()?.run(start)

        LOG.info("Registering base registration values...")

        LOG.fine("Registering base protocol channels...")
        Registration.register(RegistrarType.PROTOCOL_CHANNELS, listOf(
            HeartbeatChannel,
            SocialChannel,
            VoiceChatChannel,
            MovementChannel,
            GameplayChannel,
        ))

        LOG.fine("Registering base commands...")
        Registration.register(RegistrarType.COMMANDS, listOf(
            common.command.Dreath(),
            common.command.Help(),
            common.command.Clear(),
            common.command.Mods(),
            common.command.Stop()
        ))
        LOG.success("Registered base registration values ${start.elapsedMs()} into startup.")

        if (loadMods) {
            LOG.fine("Enabling mods...")
            ModLoader.enable()
            LOG.success("All mods were successfully enabled.")
        } // No else, because we don't need to send the same message twice.

        LOG.success(Ansi.bold("Done! Took ${start.elapsedMs()}."))

        CONSOLE?.signalStartupDone()
        keepAliveProcess()
    }

    /**
     * Called when the game ends. Shuts down the game.
     */
    private fun end() {
        LOG.info("Shutting down...")

        LOG.info("Stopping console, commands will no longer be received...")
        CONSOLE?.stop()

        LOG.info("Unloading ${ModLoader.loaded()} mods...")
        ModLoader.unload()

        SocketManager.close()

        ENVIRONMENT.instance()?.end()

        LOG.success("Done, bye!")
    }
}
