package common.util

import common.Environment
import java.net.URI
import kotlin.io.path.absolutePathString

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

    enum class OperatingSystem(val displayName: String, val company: String?, val device: String?, val libraryFileName: (String) -> String, val group: Group, val platform: Platform) {
        WINDOWS("Windows", "Microsoft", null, { "$it.dll" }, Group.WINDOWS_NT, Platform.DESKTOP),
        LINUX("Linux", null, null, { "lib$it.so" }, Group.UNIX, Platform.DESKTOP),
        MAC_OS("macOS", "Apple", "Mac", { "lib$it.dylib" }, Group.UNIX, Platform.DESKTOP),
        ANDROID("Android", "Google", null, { "lib$it.so" }, Group.UNIX, Platform.MOBILE),
        IOS("iOS", "Apple", "iPhone", { "lib$it.dylib" }, Group.UNIX, Platform.MOBILE),
        FREEBSD("FreeBSD", null, null, { "lib$it.so" }, Group.UNIX, Platform.DESKTOP),
        CHROME_OS("ChromeOS", "Google", "Chromebook", { "lib$it.so" }, Group.UNIX, Platform.DESKTOP),
        HORIZON_OS("Horizon OS", "Nintendo", "Nintendo Switch", { "$it.dll" }, Group.OTHER, Platform.CONSOLE),
        ORBIS_OS("Orbis OS", "Sony", "PlayStation 4", { "lib$it.sprx" }, Group.UNIX, Platform.CONSOLE),
        UNKNOWN("Unknown", null, null, { it }, Group.OTHER, Platform.OTHER);

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
