package common.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

object InternalConstants {
    const val DEFAULT_WINDOW_SIZE_X = 800
    const val DEFAULT_WINDOW_SIZE_Y = 600

    val OPERATING_SYSTEM = SystemInfo.OperatingSystem.ofOsName()

    val JSON: Json = @OptIn(ExperimentalSerializationApi::class) (Json {
        prettyPrint = true
        useAlternativeNames = false
        prettyPrintIndent = "  "
        namingStrategy = JsonNamingStrategy.KebabCase
        decodeEnumsCaseInsensitive = true
        allowComments = true
    })

    const val JSON_PATH_SPLITTER = "."
}
