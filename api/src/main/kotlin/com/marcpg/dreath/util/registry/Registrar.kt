package com.marcpg.dreath.util.registry

/**
 * Any type of registrar, used to register things during startup or at runtime.
 * @see Registration
 * @author MarcPG
 * @since 0.1.0
 */
interface Registrar<T> {
    /** This registrar's underlying type. */
    val type: RegistrarType

    /**
     * Registers a new instance to this registrar.
     */
    fun register(instance: T)
}

/**
 * A registrar type used for simplicity.
 * Usually, you do not need to create your own instances of this.
 * @author MarcPG
 * @since 0.1.0
 */
class RegistrarType private constructor(val name: String) {
    companion object {
        val COMMANDS = RegistrarType("commands")
    }
}
