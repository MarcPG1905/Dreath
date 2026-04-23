package com.marcpg.dreath.util.registry

import com.marcpg.dreath.command.CommandLike
import com.marcpg.dreath.event.Event
import com.marcpg.dreath.world.feature.Feature
import protocol.channel.Channel

/**
 * Any type of registrar, used to register things during startup or at runtime.
 *
 * @see Registration
 *
 * @property type This registrar's underlying type.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface Registrar<T> {
    val type: RegistrarType<T>

    /** Registers a new instance to this registrar. */
    fun register(instance: T)

    /** Returns a list of all loaded instances. */
    fun loaded(): List<T>
}

/**
 * A registrar type used for simplicity.
 * Usually, you do not need to create your own instances of this.
 * @author MarcPG
 * @since 0.1.0
 */
class RegistrarType<T> private constructor(val name: String) {
    companion object {
        /** Registrar for commands using the [com.marcpg.dreath.command.CommandBuilder] DSL via [com.marcpg.dreath.command.command]. */
        val COMMANDS = RegistrarType<CommandLike>("commands")

        /** Registrar for global features applied to each world at the end of all features. */
        val FEATURES = RegistrarType<Feature>("features")

        /** Registrar for custom events extending [Event]. */
        val EVENTS = RegistrarType<Event>("events")

        /** Registrar for custom channels to be used in the protocol. */
        val PROTOCOL_CHANNELS = RegistrarType<Channel>("protocol.channels")
    }
}
