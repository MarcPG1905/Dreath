package com.marcpg.dreath.util

import kotlin.random.Random

/**
 * Represents a seed and just uses a [Long].
 * This is done to allow for later change to something like strings or simplification to 32-bit integers if needed.
 *
 * @author MarcPG
 * @since 0.1.0
 */
@JvmInline
value class Seed(val value: Long) {
    companion object {
        /** The default seed. */
        val ZERO = Seed(0L)

        /**
         * Constructs the seed from the provided string, assuming it matches the seed format.
         * @return A seed, or `null` if the format is incorrect.
         */
        fun fromString(s: String): Seed? = s.toLongOrNull()?.let { Seed(it) }

        /**
         * Gets a randomized seed using Kotlin's [Random] utilities.
         * @return A random seed.
         */
        fun random(): Seed = Seed(Random.nextLong())
    }

    override fun toString(): String = "Seed($value)"
}
