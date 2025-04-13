package com.marcpg.util

import com.marcpg.log.DreathLoggerFactory
import io.github.vyfor.kpresence.RichClient
import io.github.vyfor.kpresence.event.ReadyEvent
import io.github.vyfor.kpresence.rpc.ActivityType
import java.net.URI

object DiscordRichPresence {
    fun run() {
        val client = RichClient(1360287235714449581)
        client.logger = DreathLoggerFactory.create("Discord-RPC")
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

private val DISCORD_INVITE_REGEX = """^(?:(?:https?://)?(?:www\.)?(?:discord(?:app)?\.com/invite/|(?:discord\.gg/|\.gg/))([A-Za-z0-9-]+)|([A-Za-z0-9-]+))$""".toRegex()

fun resolveDiscordInvite(input: String): URI? {
    return DISCORD_INVITE_REGEX.find(input)?.groupValues?.let {
        URI("https://discord.gg/${it[1]}")
    }
}
