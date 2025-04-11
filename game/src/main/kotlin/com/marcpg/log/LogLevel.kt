package com.marcpg.log

import com.marcpg.libpg.color.Ansi

enum class LogLevel(val abb: String, val ansi: String, val weight: Int) {
    FINE("FINE", Ansi.GRAY.get(), 0),
    CONFIG("CONF", Ansi.DARK_GRAY.get(), 0),
    INFO("INFO", Ansi.WHITE.get(), 1),
    WARN("WARN", Ansi.YELLOW.get(), 2),
    ERROR("ERR", Ansi.RED.get(), 3)
}