package com.marcpg.dreath.util

/**
 * Represents any version of anything. It includes very thorough information about the version.
 * This data class uses very small units for each field, so it is very compact, even tho it does have a lot of fields.
 *
 * A version in this format should usually contain these fields.
 * All fields marked with `!` are required and do not have a default value:
 *
 * - Version ID **!** - `0`-`65535`
 * - Module Name **!**
 * - Version **!** - Major, minor, patch, each `0`-`255`
 * - Release Type - [ReleaseType.ALPHA], [ReleaseType.BETA], [ReleaseType.RELEASE]
 * - Required JVM Version - Only first part, e.g. 8; 11; 17; 21
 *
 * These fields are derived from the above fields, but can also be specified manually:
 *
 * - String Representation
 * - Packed Version (major, minor, patch as single [UInt] for comparisons)
 */
data class DreathVersion(
    val verId: UShort,
    val moduleName: String,
    val ver: Triple<UByte, UByte, UByte>,
    val verName: String = "${ver.first}.${ver.second}.${ver.third}",
    val verType: ReleaseType = ReleaseType.RELEASE,
    val verPacked: UInt = packVersion(ver),
    val jvmRequired: Int = Runtime.version().feature(),
) {
    companion object {
        private fun packVersion(ver: Triple<UByte, UByte, UByte>): UInt {
            return (ver.first.toUInt() shl 16) or
                    (ver.second.toUInt() shl 8) or
                    ver.third.toUInt()
        }

        private fun packVersion(ver: String): UInt {
            val separated = ver.split(".").map { it.toUInt() }.toMutableList()
            return (separated.getOrElse(0) { 0u } shl 16) or
                    (separated.getOrElse(1) { 0u } shl 8) or
                    separated.getOrElse(2) { 0u }
        }
    }

    /**
     * Gets this version as a full [String] representation in the format `<module> <name> (<id>)`
     *
     * If specified, this will also include the version's ID.
     *
     * ### Example
     *
     * - Module Name: `Dreath`
     * - Version: `1; 8; 7`
     * - Version ID: `69`
     *
     * Output:
     * ```
     * Dreath 1.8.7 (69)
     * ```
     */
    fun fullString(includeId: Boolean = false): String {
        return if (includeId) {
            "$moduleName $verName ($verId)"
        } else {
            "$moduleName $verName"
        }
    }

    /**
     * Compares this to another version.
     *
     * If the other version is of the same module, it will compare using the [ID][verId], otherwise it will compare using the [packed version][verPacked].
     */
    operator fun compareTo(other: DreathVersion): Int {
        return if (other.moduleName == moduleName) {
            verId.compareTo(other.verId)
        } else {
            verPacked.compareTo(other.verPacked)
        }
    }

    /**
     * Compares this to another version in [String] format. This uses the [packed version][verPacked] to compare.
     */
    operator fun compareTo(other: String): Int {
        return verPacked.compareTo(packVersion(other))
    }

    /**
     * Converts this version to a [String].
     * Uses [fullString] with `includeId` set to `false`.
     */
    override fun toString(): String = fullString()
}

/**
 * Represents a release type, which is either alpha, beta, or a release.
 */
enum class ReleaseType {
    /** Alpha - Unstable */ ALPHA,
    /** Beta - User Feedback */ BETA,
    /** Release - Production Usage */ RELEASE
}
