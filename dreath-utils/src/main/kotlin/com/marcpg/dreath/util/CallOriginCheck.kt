package com.marcpg.dreath.util

object CallOriginCheck {
    fun require(allowed: Set<String>, limit: Int = 12) {
        val stackTrace = Throwable().stackTrace
        val thisClass = stackTrace.getOrNull(2)?.className ?: return

        val illegal = stackTrace
            .drop(2).take(limit)
            .none { frame -> frame.className == thisClass || frame.className in allowed }

        if (illegal)
            error("Illegal call origin:\n ${stackTrace.joinToString("\n") { "  at $it" }}")
    }
}
