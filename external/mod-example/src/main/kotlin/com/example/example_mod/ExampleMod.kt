package com.example.example_mod

import com.marcpg.dreath.DreathMod
import com.marcpg.dreath.command.command
import com.marcpg.dreath.event.Event
import com.marcpg.dreath.scsdfwt.SDF
import com.marcpg.dreath.util.Seed
import com.marcpg.dreath.util.registry.RegistrarType
import com.marcpg.dreath.util.registry.Registration
import com.marcpg.dreath.world.feature.Feature

object ExampleMod : DreathMod() {
    override fun enable() {
        logger.info("Hello from ExampleMod!")

        Registration.register(RegistrarType.COMMANDS, command("test") {
            flag("test", 't', "This is a test flag.")
        }.build())

        Registration.register(RegistrarType.FEATURES, object : Feature {
            override val name: String = "test"
            override val seed: Seed = Seed.ZERO
            override fun getSDF(): SDF = SDF { x, y, z -> 0.0 }
        })

        Registration.register(RegistrarType.EVENTS, object : Event() {
            override val isStoppable: Boolean = false
            override val isCancellable: Boolean = false
        })
    }

    override fun disable() {
        logger.info("Bye, ExampleMod out!")
    }
}
