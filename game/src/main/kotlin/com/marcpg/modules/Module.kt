package com.marcpg.modules

enum class Module(val requireData: Boolean = false) {
    NONE;

    fun kebabCase(): String {
        return name.lowercase().replace('_', '-')
    }
}