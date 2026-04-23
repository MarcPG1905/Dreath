package com.marcpg.dreath.util

import java.util.concurrent.atomic.AtomicInteger

class AtomicUInt(initial: UInt = 0u) {
    private val backing = AtomicInteger(initial.toInt())

    fun get(): UInt = backing.get().toUInt()
    fun set(value: UInt) = backing.set(value.toInt())
    fun getAndIncrement(): UInt = backing.getAndIncrement().toUInt()
    fun incrementAndGet(): UInt = backing.incrementAndGet().toUInt()
    fun compareAndSet(expect: UInt, update: UInt) =
        backing.compareAndSet(expect.toInt(), update.toInt())
}
