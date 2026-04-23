package com.marcpg.dreath.util

/**
 * A simple version in the major/minor/patch format (SemVer).
 *
 * @param major Incompatible API changes.
 * @param minor Adding functionality in a backward compatible manner.
 * @param patch Backward compatible bug fixes.
 *
 * @author MarcPG
 * @since 0.1.0
 */
data class SimpleVersion(val major: Int, val minor: Int, val patch: Int): Comparable<SimpleVersion> {
    companion object {
        /**
         * Converts a string to a [SimpleVersion].
         *
         * This first splits by the `.` character and converts all split parts to int, defaulting to 0 if it is not an int.
         * Then it constructs a SimpleVersion using the first three elements in the split list, again defaulting to 0.
         */
        fun fromString(s: String): SimpleVersion {
            val parts = s.split(".").map { it.toIntOrNull() ?: 0 }
            return SimpleVersion(parts.getOrElse(0) { 0 }, parts.getOrElse(1) { 0 }, parts.getOrElse(2) { 0 })
        }
    }

    override fun compareTo(other: SimpleVersion): Int = compareValuesBy(this, other, { it.major }, { it.minor }, { it.patch })
}
