package com.marcpg.modules

enum class Module(val requireData: Boolean = false) {
    LOCATION_NAME_GEN,
    MARKOV_CHAIN,
    NONE;

    fun kebabCase(): String {
        return name.lowercase().replace('_', '-')
    }
}