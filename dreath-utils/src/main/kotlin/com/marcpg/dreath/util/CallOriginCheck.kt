package com.marcpg.dreath.util

/**
 * Utilities for verifying a method was called from a specific location.
 * This should not be used as a guaranteed check, but instead as an additional layer on top of trusting users.
 * @author MarcPG
 * @since 0.1.0
 */
object CallOriginCheck {
    /**
     * Verifies a method was called from a specific location and throws an error if it was not.
     * @param allowed A list of call origins/locations which are allowed.
     * @param limit The maximum amount of method calls the origin can be away to still be considered allowed.
     */
    fun require(allowed: Set<String>, limit: Int = 6) {
        val stackTrace = Throwable().stackTrace
        val thisClass = stackTrace.getOrNull(2)?.className ?: return

        val illegal = stackTrace
            .drop(2).take(limit)
            .none { frame -> frame.className == thisClass || frame.className in allowed }

        if (illegal)
            error("Illegal call origin:\n ${stackTrace.joinToString("\n") { "  at $it" }}")
    }
}
