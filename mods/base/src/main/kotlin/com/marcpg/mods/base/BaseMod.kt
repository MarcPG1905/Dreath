package com.marcpg.mods.base

import com.marcpg.dreath.DreathMod

object BaseMod : DreathMod() {
    override fun enable() {
        logger.info("Enabling base game content.")
    }

    override fun disable() {
        logger.info("Disabling base game content.")
    }
}
