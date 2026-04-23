package com.marcpg.dreath.event

/**
 * Abstract class for any type of event which can be registered, be called, and have a [Result].
 * @author MarcPG
 * @since 0.1.0
 */
abstract class Event {
    /**
     * This event's currently set result.
     *
     * Assigning this property will also check if the new value is allowed by [isStoppable] and [isCancellable].
     * If not allowed, it will throw an [IllegalArgumentException].
     */
    var result: Result = Result.ALLOW
        set(value) {
            require(value.predicate(this)) { "'${javaClass.name}' does not allow result $value!" }
            field = value
        }

    /** If this event is stoppable, meaning it can be set to [Result.STOP_CHAIN]. */
    open val isStoppable: Boolean = false

    /** If this event is cancellable, meaning it can be set to [Result.CANCEL]. */
    open val isCancellable: Boolean = false

    /**
     * If this event is canceled. Equal to `result.isCancelled`.
     * @see Result.isCancelled
     */
    val isCancelled: Boolean get() = result.isCancelled

    /**
     * If this event is canceled. Equal to `result.isStopped`.
     * @see Result.isStopped
     */
    val isStopped: Boolean get() = result.isStopped
}

/**
 * The result of an [Event].
 * @author MarcPG
 * @since 0.1.0
 */
enum class Result(
    internal val predicate: (Event) -> Boolean,

    /** If the result counts as stopped. Currently only [STOP_CHAIN] and [STOP_AND_CANCEL]. */
    val isStopped: Boolean = false,

    /** If the result counts as canceled. Currently only [CANCEL] and [STOP_AND_CANCEL]. */
    val isCancelled: Boolean = false,
) {
    /** Default - Event is allowed and nothing changes. */
    ALLOW({ true }),

    /** Allowed, but no more listeners should be called. */
    STOP_CHAIN({ it.isStoppable }, isStopped = true),

    /** The event is canceled and all changes should be undone. */
    CANCEL({ it.isCancellable }, isCancelled = true),

    /** The event is canceled and no more listeners should be called. */
    STOP_AND_CANCEL({ it.isStoppable && it.isCancellable }, isStopped = true, isCancelled = true),
}
