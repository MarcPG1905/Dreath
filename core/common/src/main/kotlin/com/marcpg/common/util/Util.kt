package com.marcpg.common.util

import com.marcpg.common.Environment
import com.marcpg.common.Game
import java.io.BufferedReader
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.absolutePathString

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
        return "${OperatingSystem.ofOsName().completeName()} ${prop("os.version")} (${prop("os.arch")}) - Working at ${prop("user.dir")}"
    }

    fun user() : String {
        return "${prop("user.name")} (${prop("user.home")}) - Data under ${Environment.current().instance()?.dir?.absolutePathString() ?: "Unknown"}"
    }

    fun prop(key: String, def: String = "UNKNOWN") : String {
        return System.getProperty(key, def)
    }

    enum class OperatingSystem(val displayName: String, val company: String?, val device: String?, val group: Group, val platform: Platform) {
        WINDOWS("Windows", "Microsoft", null, Group.WINDOWS_NT, Platform.DESKTOP),
        LINUX("Linux", null, null, Group.UNIX, Platform.DESKTOP),
        MAC_OS("macOS", "Apple", "Mac", Group.UNIX, Platform.DESKTOP),
        ANDROID("Android", "Google", null, Group.UNIX, Platform.MOBILE),
        IOS("iOS", "Apple", "iPhone", Group.UNIX, Platform.MOBILE),
        FREEBSD("FreeBSD", null, null, Group.UNIX, Platform.DESKTOP),
        CHROME_OS("ChromeOS", "Google", "Chromebook", Group.UNIX, Platform.DESKTOP),
        HORIZON_OS("Horizon OS", "Nintendo", "Nintendo Switch", Group.OTHER, Platform.CONSOLE),
        ORBIS_OS("Orbis OS", "Sony", "PlayStation 4", Group.UNIX, Platform.CONSOLE),
        UNKNOWN("Unknown", null, null, Group.OTHER, Platform.OTHER);

        companion object {
            fun ofOsName(name: String = System.getProperty("os.name")): OperatingSystem {
                val osName = name.lowercase()
                return when { // Special order to now shadow stuff.
                    osName.contains("android") -> ANDROID
                    osName.contains("ios") -> IOS
                    osName.contains("mac") || osName.contains("darwin") -> MAC_OS
                    osName.contains("chrome") -> CHROME_OS
                    osName.contains("horizon") -> HORIZON_OS
                    osName.contains("orbis") -> ORBIS_OS
                    osName.contains("bsd") -> FREEBSD
                    osName.startsWith("windows") -> WINDOWS
                    osName.contains("linux") -> LINUX
                    else -> UNKNOWN
                }
            }
        }

        enum class Group(val nullPath: String) {
            WINDOWS_NT("NUL"),
            UNIX("/dev/null"),
            OTHER("");
        }
        enum class Platform { DESKTOP, MOBILE, CONSOLE, OTHER }

        fun completeName(): String = "$displayName ${if (device != null) "($device)" else ""} ${if (company != null) "by $company" else ""}".trimEnd()
    }
}

private val DISCORD_INVITE_REGEX = """^(?:(?:https?://)?(?:www\.)?(?:discord(?:app)?\.com/invite/|(?:discord\.gg/|\.gg/))([A-Za-z0-9-]+)|([A-Za-z0-9-]+))$""".toRegex()

fun resolveDiscordInvite(input: String): URI? {
    return DISCORD_INVITE_REGEX.find(input)?.groupValues?.let {
        URI("https://discord.gg/${it[1]}")
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
