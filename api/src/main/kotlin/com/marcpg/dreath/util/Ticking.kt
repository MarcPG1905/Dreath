package com.marcpg.dreath.util

/**
 * Anything synced to some kind of ticking with a central `tick(Tick)` method to be executed.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface Ticking {
    /** Should be executed when, whatever this is listening to, ticks. */
    fun tick(tick: Tick)

    /**
     * Represents a tick with some minimal utility methods.
     * @param value The number of the tick since the object's lifetime start.
     */
    data class Tick(val value: Long) {
        /**
         * Checks whether this tick should be at the first tick of this second.
         *
         * Keep in mind that this does not know of skipped ticks or moments of lower or higher TPS than the set [ticksPerSecond].
         *
         * @param ticksPerSecond How many ticks should happen in one second, for the calculation.
         */
        fun isSecond(ticksPerSecond: Long): Boolean = value % ticksPerSecond == 0L
    }
}
