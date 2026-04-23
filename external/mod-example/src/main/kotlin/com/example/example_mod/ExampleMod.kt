package com.example.example_mod

import com.marcpg.cotton.style.StyleDecoration
import com.marcpg.cotton.util.Color
import com.marcpg.cotton.util.appendSpace
import com.marcpg.cotton.util.translatable
import com.marcpg.dreath.DreathMod
import com.marcpg.dreath.command.command
import com.marcpg.dreath.event.Event
import com.marcpg.dreath.event.EventBus
import com.marcpg.dreath.event.Result
import com.marcpg.dreath.event.events.ServerPreTickEvent
import com.marcpg.dreath.scsdfwt.SDF
import com.marcpg.dreath.util.Seed
import com.marcpg.dreath.util.registry.RegistrarType
import com.marcpg.dreath.util.registry.Registration
import com.marcpg.dreath.world.feature.Feature

object ExampleMod : DreathMod() {
    override fun enable() {
        // Little introduction message to indicate the mod is starting up.
        logger.info("Hello from ${info.name}!")

        // Registering a test command using the registry.
        Registration.register(RegistrarType.COMMANDS, command("test") {
            flag("test", 't', "This is a test flag.")
        }.build())

        // Registering a simple feature command using the registry.
        Registration.register(RegistrarType.FEATURES, object : Feature {
            // The name of this feature, which should be unique to this mod's feature list.
            override val name: String = "test"

            // The seed used for this feature.
            override val seed: Seed = Seed.ZERO

            // The signed distance function used on the CPU.
            override fun getSDF(): SDF = SDF { x, y, z -> 0.0 }

            // The signed distance function used on the GPU, should be effectively the same as the CPU's SDF.
            override fun getShaderSDF(): String = """
                float $glslFuncName(vec3 pos) {
                    return 0.0;
                }
            """.trimIndent()
        })

        // Registering a simple event using the registry.
        Registration.register(RegistrarType.EVENTS, object : Event() {
            override val isStoppable: Boolean = false
            override val isCancellable: Boolean = false
        })

        // Listening to the ServerPreTickEvent, which gets executed before a server-side tick is executed.
        EventBus.register<ServerPreTickEvent> {
            // Cancelling this event, effectively making the server frozen.
            it.result = Result.CANCEL
        }

        logger.sendChat(translatable("some.test.key", color = Color.RED).appendSpace().append(translatable("some.test.key", decorations = mapOf(StyleDecoration.BOLD to true))))
    }

    override fun disable() {
        // Little goodbye message to indicate the mod is now shutting down.
        logger.info("Bye, ${info.name} out!")
    }
}
