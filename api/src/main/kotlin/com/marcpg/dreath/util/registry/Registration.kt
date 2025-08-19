package com.marcpg.dreath.util.registry

/**
 * A utility class for registering things during startup or at runtime.
 * This will automatically figure out which [Registrar] to use based on the provided [RegistrarType].
 * @see Registration
 * @author MarcPG
 * @since 0.1.0
 */
@Suppress("UNCHECKED_CAST")
object Registration {
    private lateinit var REGISTRARS: Map<RegistrarType, Registrar<*>>

    /**
     * Registers a single instance to the specified registrar type.
     */
    fun <T> register(type: RegistrarType, instance: T) {
        require(type in REGISTRARS) { "No registrar found for type ${type.name}" }

        val registrar = REGISTRARS[type] as Registrar<T>
        registrar.register(instance)
    }

    /**
     * Registers multiple instances to the specified registrar type.
     */
    fun <T> register(type: RegistrarType, instances: Iterable<T>) {
        require(type in REGISTRARS) { "No registrar found for type ${type.name}" }

        val registrar = REGISTRARS[type] as Registrar<T>
        instances.forEach { registrar.register(it) }
    }

    /**
     * Initialization method used by the internal game.
     *
     * **A mod should under no circumstances call This!**
     */
    fun initialize(map: Map<RegistrarType, Registrar<*>>) {
        REGISTRARS = map
    }
}
