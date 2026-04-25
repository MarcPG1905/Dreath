package com.marcpg.dreath.util

import java.util.concurrent.atomic.AtomicInteger

/**
 * A [UInt] value that may be updated atomically.
 *
 * Internally, this is just a wrapper for [AtomicInteger], which automatically converts from/to [UInt] in its operations.
 * @see AtomicInteger
 *
 * @author MarcPG
 * @since 0.1.0
 */
class AtomicUInt(initial: UInt = 0u) {
    private val backing = AtomicInteger(initial.toInt())

    /** Gets the current value. */
    fun get(): UInt = backing.get().toUInt()

    /** Sets the current value. */
    fun set(value: UInt) = backing.set(value.toInt())

    /** Gets the current value and then increments it by 1. */
    fun getAndIncrement(): UInt = backing.getAndIncrement().toUInt()

    /** Increments the current value by 1 and then gets it. */
    fun incrementAndGet(): UInt = backing.incrementAndGet().toUInt()

    /** @see AtomicInteger.compareAndSet */
    fun compareAndSet(expect: UInt, update: UInt) =
        backing.compareAndSet(expect.toInt(), update.toInt())
}
