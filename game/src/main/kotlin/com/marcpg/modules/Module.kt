package com.marcpg.modules

enum class Module(val requireData: Boolean = false) {
    MARKOV_CHAIN,
    NONE;

    fun kebabCase(): String {
        return name.lowercase().replace('_', '-')
    }
}