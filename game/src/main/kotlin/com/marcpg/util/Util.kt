package com.marcpg.util

import com.marcpg.Game
import java.io.BufferedReader
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object ResourceUtil {
    fun readLinesFromResource(name: String): List<String> {
        val stream = Game::class.java.getResourceAsStream("/$name") ?: throw IllegalStateException("No $name in attached resources.")
        return stream.bufferedReader().use(BufferedReader::readLines)
    }

    fun readBytesFromResource(name: String): ByteArray {
        val stream = Game::class.java.getResourceAsStream("/$name") ?: throw IllegalStateException("No $name in attached resources.")
        return stream.readAllBytes()
    }

    fun saveResource(name: String, destination: Path) {
        Game::class.java.getResourceAsStream("/$name").use { stream ->
            Files.copy(stream ?: throw IllegalStateException("No $name in attached resources."), destination)
        }
    }

    fun saveResource(name: String, destination: File) {
        saveResource(name, destination.toPath())
    }
}

object SystemInfo {
    fun jvm() : String {
        return "Java ${prop("java.version")} (${prop("java.vendor")} via ${prop("java.vendor.url")})"
    }

    fun os() : String {
        return "${prop("os.name")} ${prop("os.version")} (${prop("os.arch")}) - Working at ${prop("user.dir")}"
    }

    fun user() : String {
        return "${prop("user.name")} (${prop("user.home")}) - Data under ${prop("user.home")}/.config/dreath/"
    }

    fun prop(key: String, def: String = "UNKNOWN") : String {
        return System.getProperty(key, def)
    }
}

fun ULong.toByteArray(): ByteArray {
    return ByteArray(8) { index ->
        (this shr (56 - index * 8) and 0xFFu).toByte()
    }
}

fun ByteArray.toULong(): ULong {
    var result = 0UL
    for (i in 0 until 8) {
        result = result or (this[i].toULong() shl (8 * (7 - i)))
    }
    return result
}

fun String.makeCapitalized(): String {
    return lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.truncate(maxLength: Int = 48): String {
    return if (length <= maxLength) this else take(maxLength - 3) + "..."
}

inline fun <reified T : Enum<T>> enumValueNoCase(name: String): T =
    enumValues<T>().firstOrNull { it.name.equals(name, ignoreCase = true) }
        ?: throw IllegalArgumentException("No enum constant ${T::class.qualifiedName}.$name")
