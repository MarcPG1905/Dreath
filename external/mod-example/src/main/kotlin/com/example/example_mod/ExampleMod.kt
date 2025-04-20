package com.example.example_mod

import com.marcpg.dreath.DreathMod

object ExampleMod : DreathMod() {
    override fun enable() {
        logger.info("Hello from ExampleMod!")
    }

    override fun disable() {
        logger.info("Bye, ExampleMod out!")
    }
}