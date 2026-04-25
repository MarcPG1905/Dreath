package com.marcpg.dreath.util

import kotlin.math.pow

/**
 * Creates a [ULongRange] including all values from 0 until the maximum number which can be represented with the specified amount of bits.
 * The maximum number is calculated using `0 ..< (2^bits)`, so 2 for [bits] would result in the range `[0, 1, 2, 3]`.
 * @param bits The amount of bits of the maximum value.
 */
fun rangeForBits(bits: Int): ULongRange = 0uL..<2.0.pow(bits).toULong()

/**
 * Gets the first value in this list which is not also in the [reserved] list.
 * @throws [NoSuchElementException] if no such element is found.
 */
fun <T : Comparable<T>> Iterable<T>.firstNotIn(reserved: Iterable<T>): T = first { it !in reserved }

/** Gets the first value in this list which is not also in the [reserved] list, or `null` if no element was found. */
fun <T : Comparable<T>> Iterable<T>.firstNotInOrNull(reserved: Iterable<T>): T? = firstOrNull { it !in reserved }

/**
 * Gets the first number for the amount of bits which matches the specified [filter].
 * @throws [NoSuchElementException] if no such number is found.
 * @see rangeForBits
 */
fun findInRangeForBits(bits: Int, filter: (ULong) -> Boolean): ULong = rangeForBits(bits).first(filter)

/**
 * Gets the first number for the amount of bits which matches the specified [filter], or `null` if no number was found.
 * @see rangeForBits
 */
fun findInRangeForBitsOrNull(bits: Int, filter: (ULong) -> Boolean): ULong? = rangeForBits(bits).firstOrNull(filter)

/**
 * Gets the first number for the amount of bits which is not also in the [reserved] list.
 * @throws [NoSuchElementException] if no such number is found.
 * @see rangeForBits
 * @see firstNotIn
 */
fun findInRangeForBitsNotIn(bits: Int, reserved: Iterable<ULong>): ULong = rangeForBits(bits).firstNotIn(reserved)

/**
 * Gets the first number for the amount of bits which is not also in the [reserved] list, or `null` if no number was found.
 * @see rangeForBits
 * @see firstNotIn
 */
fun findInRangeForBitsNotInOrNull(bits: Int, reserved: Iterable<ULong>): ULong? = rangeForBits(bits).firstNotInOrNull(reserved)
