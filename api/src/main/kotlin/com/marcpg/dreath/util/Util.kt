package com.marcpg.dreath.util

import kotlin.random.Random

/**
 * Represents a seed and just uses a [Long].
 * This is done to allow for later change to something like strings or simplification to 32-bit integers if needed.
 * @since 0.1.0
 */
@JvmInline
value class Seed(val value: Long) {
    companion object {
        val ZERO = Seed(0L)

        fun fromString(s: String): Seed? = s.toLongOrNull()?.let { Seed(it) }
        fun random(): Seed = Seed(Random.nextLong())
    }

    override fun toString(): String = "Seed($value)"
}
