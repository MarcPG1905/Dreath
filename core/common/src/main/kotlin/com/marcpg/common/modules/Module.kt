package com.marcpg.common.modules

enum class Module(val requireData: Boolean = false) {
    LOCATION_NAME_GEN,
    MARKOV_CHAIN,
    PROTOCOL,
    NONE;

    fun kebabCase(): String {
        return name.lowercase().replace('_', '-')
    }
}
