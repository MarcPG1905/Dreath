package com.marcpg.util

import java.net.URI

private val DISCORD_INVITE_REGEX = """^(?:(?:https?://)?(?:www\.)?(?:discord(?:app)?\.com/invite/|(?:discord\.gg/|\.gg/))([A-Za-z0-9-]+)|([A-Za-z0-9-]+))$""".toRegex()

fun resolveDiscordInvite(input: String): URI? {
    return DISCORD_INVITE_REGEX.find(input)?.groupValues?.let {
        URI("https://discord.gg/${it[1]}")
    }
}
