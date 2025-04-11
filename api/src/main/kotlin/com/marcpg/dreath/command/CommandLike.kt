package com.marcpg.dreath.command

/**
 * Anything which can be converted to a [Command].
 * Includes anything from plain commands, to command builders, and single-command classes.
 */
interface CommandLike {
    /**
     * The method for getting the actual command.
     */
    fun asCommand(): Command
}
