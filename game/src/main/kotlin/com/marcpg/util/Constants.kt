package com.marcpg.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

object Constants {
    enum class ReleaseType { ALPHA, BETA, RELEASE }

    const val NAME = "Dreath"
    val VERSION = Triple(0, 1, 0)
    val RELEASE = ReleaseType.ALPHA
    val VERSION_STRING = "${VERSION.first}.${VERSION.second}.${VERSION.third}-${RELEASE.name.makeCapitalized()}"

    const val DEFAULT_WINDOW_SIZE_X = 800
    const val DEFAULT_WINDOW_SIZE_Y = 600
    const val TPS = 64

    const val PEG_PROTOCOL_VERSION = 1
    const val MAX_PAYLOAD_BYTES = 1024

    const val WORLD_PORT = 42068
    const val GAMEPLAY_PORT = 42069
    const val VOICE_PORT = 42070

    val JSON: Json = @OptIn(ExperimentalSerializationApi::class) (Json {
        prettyPrint = true
        useAlternativeNames = false
        prettyPrintIndent = "  "
        namingStrategy = JsonNamingStrategy.KebabCase
        decodeEnumsCaseInsensitive = true
        allowComments = true
    })
}