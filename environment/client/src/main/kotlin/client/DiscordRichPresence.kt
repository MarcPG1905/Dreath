package client

import com.marcpg.dreath.log.DreathLogger
import com.marcpg.dreath.log.dreathLogger
import com.marcpg.dreath.util.Constants
import io.github.vyfor.kpresence.RichClient
import io.github.vyfor.kpresence.event.ReadyEvent
import io.github.vyfor.kpresence.logger.ILogger
import io.github.vyfor.kpresence.rpc.ActivityType

object DiscordRichPresence {
    fun run() {
        val client = RichClient(1360287235714449581)
        client.logger = ILoggerWrapper(dreathLogger("Discord-RPC"))
        client.on<ReadyEvent> {
            update {
                type = ActivityType.GAME
                details = Constants.VERSION.fullString()
                state = "Experimental Version" // TODO: Remove this!
            }
        }
        client.connect(shouldBlock = false)
    }
}

/**
 * Utility wrapper forwarding [ILogger] to the game's [com.marcpg.dreath.log.DreathLogger].
 * This ignores all messages except for warnings and errors.
 */
internal class ILoggerWrapper(private val logger: DreathLogger) : ILogger {
    override fun debug(message: String) {}
    override fun trace(message: String) {}
    override fun info(message: String) {}

    override fun warn(message: String) = logger.warn(message)
    override fun error(message: String) = logger.error(message)

    override fun error(message: String, throwable: Throwable) = logger.error(message, throwable)
}
