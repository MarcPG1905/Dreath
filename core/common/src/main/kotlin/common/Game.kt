package common

import com.marcpg.dreath.Dreath
import com.marcpg.dreath.log.DreathLogger
import com.marcpg.dreath.log.DreathLoggerFactory
import com.marcpg.dreath.log.LoggerOwner
import com.marcpg.dreath.log.dreathLogger
import com.marcpg.dreath.util.registry.RegistrarType
import com.marcpg.dreath.util.registry.Registration
import com.marcpg.libpg.color.Ansi
import com.marcpg.libpg.util.toTitleCase
import common.channels.*
import common.command.Console
import common.mods.ModLoader
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

object Game : LoggerOwner {
    val MAIN_THREAD: Thread = Thread.currentThread()
    val ENVIRONMENT: Environment = Environment.current()
    lateinit var CLI_ARGS: CommandArguments

    override lateinit var log: DreathLogger
    lateinit var dir: Path
    internal var console: Console? = null

    var running: Boolean = true
        internal set

    /**
     * Called when the game starts. Runs the main logic of the game.
     * @param args The command line arguments used.
     */
    fun run(args: CommandArguments) {
        val start = TimeSource.Monotonic.markNow()

        CLI_ARGS = args
        dir = Files.createDirectories(CLI_ARGS.gameDir)

        // Prevents errors which aren't from the DreathLogger from being printed.
        System.setErr(FilteredOutputStream(System.err))

        Dreath.initialize({ running }, { CLI_ARGS.debug })

        CommonConfig.init()

        DreathLoggerFactory.initialize(CLI_ARGS.gameDir.resolve("logs"))
        DreathLogger.logLevel = CommonConfig.loggerLevel
        DreathLogger.ansiFormatting = CommonConfig.loggerAnsiFormatting
        log = dreathLogger("Common")

        log.config("Debug: ${if (Dreath.isDebug) "Enabled" else "Disabled"}")
        log.config("JVM: ${SystemInfo.jvm()}")
        log.config("System: ${SystemInfo.os()}")
        log.config("User: ${SystemInfo.user()}")
        log.config("Environment: ${ENVIRONMENT.name.toTitleCase()}")

        console = Console()

        onShutdownProcess { end() }

        log.fine("Starting Dreath main logic...")

        SocketManager.open(ENVIRONMENT == Environment.SERVER)
        ChannelManager.initialize(ChannelRegistrar)

        val loadMods = !CLI_ARGS.noMods && CommonConfig.loadMods
        if (loadMods) {
            log.fine("Loading mods...")
            loadMods()
        } else {
            log.config("Skipping mod loading due to flag or configuration.")
        }

        ENVIRONMENT.instance()?.run(start)

        initRegistry()

        if (loadMods) {
            log.fine("Enabling mods...")
            ModLoader.enable()
            log.success("All mods were successfully enabled.")
        }

        log.success(Ansi.bold("Done! Took ${start.elapsedMs()}."))

        // Creates a virtual thread which is constantly halted to keep the process alive.
        keepAliveProcess()
    }

    private fun loadMods() {
        val loaded = ModLoader.init()
        if (loaded.first > 0) {
            log.success("Found and initialized ${loaded.first} mods.")
        } else {
            log.info("No mods found.")
        }
        log.info("Total mods loaded: ${loaded.second}")
    }

    private fun initRegistry() {
        log.fine("Registering base registration values...")

        log.fine("Registering base protocol channels...")
        Registration.register(RegistrarType.PROTOCOL_CHANNELS, listOf(
            HeartbeatChannel,
            SocialChannel,
            VoiceChatChannel,
            MovementChannel,
            GameplayChannel,
        ))

        log.fine("Registering base commands...")
        Registration.register(RegistrarType.COMMANDS, listOf(
            common.command.Dreath(),
            common.command.Help(),
            common.command.Clear(),
            common.command.Mods(),
            common.command.Stop()
        ))
        log.success("Registered base registration values.")
    }

    /**
     * Called when the game ends. Shuts down the game.
     */
    private fun end() {
        log.info("Shutting down...")

        log.info("Stopping console, commands will no longer be received...")
        console?.stop()

        log.info("Unloading ${ModLoader.loaded()} mods...")
        ModLoader.unload()

        SocketManager.close()

        ENVIRONMENT.instance()?.end()

        log.success("Done, bye!")
    }
}
