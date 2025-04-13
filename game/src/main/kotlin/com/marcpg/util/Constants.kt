package com.marcpg.util

import com.marcpg.dreath.util.DreathVersion
import com.marcpg.dreath.util.ReleaseType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

object Constants {
    val VERSION = DreathVersion(
        verId = 0u,
        moduleName = "Dreath",
        ver = Triple(0u, 1u, 0u),
        verType = ReleaseType.ALPHA,
    )

    val PROTOCOL_VERSION = DreathVersion(
        verId = 0u,
        moduleName = "Peg-Protocol",
        ver = Triple(0u, 1u, 0u),
        verType = ReleaseType.ALPHA,
    )

    const val DEFAULT_WINDOW_SIZE_X = 800
    const val DEFAULT_WINDOW_SIZE_Y = 600
    const val TPS = 64

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