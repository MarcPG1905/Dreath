package com.marcpg.dreath.command

/**
 * Utility interface that can be extended for single-class commands.
 * Only for convenience and no actual benefit over building commands separately.
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
    override fun asCommand(): Command = constructCommand()
}