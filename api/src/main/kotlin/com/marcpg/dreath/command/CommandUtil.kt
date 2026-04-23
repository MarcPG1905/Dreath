package com.marcpg.dreath.command

/**
 * Anything that can be converted to a [Command].
 * Includes anything from plain commands, to command builders, and single-command classes.
 *
 * @author MarcPG
 * @since 0.1.0
 */
fun interface CommandLike {
    /** The method for getting the actual command. */
    operator fun invoke(): AbstractCommand
}

/**
 * Utility interface that can be extended for single-class commands.
 * Only for convenience and no actual benefit over building commands separately.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface CommandClass : CommandLike {
    /**
     * The method for getting the actual command.
     *
     * Override this if you want a single-class command.
     */
    fun constructCommand(): Command

    /**
     * Please do not override this method, and use [constructCommand] instead.
     * This just returns the output of [constructCommand].
     */
    override operator fun invoke(): AbstractCommand = constructCommand()
}
