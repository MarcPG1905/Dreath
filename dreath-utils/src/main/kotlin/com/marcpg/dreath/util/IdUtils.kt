package com.marcpg.dreath.util

import kotlin.math.pow

fun rangeForBits(bits: Int): ULongRange = 0uL..<2.0.pow(bits).toULong()

fun <T : Comparable<T>> Iterable<T>.firstNotIn(reserved: Iterable<T>): T = first { it !in reserved }
fun <T : Comparable<T>> Iterable<T>.firstNotInOrNull(reserved: Iterable<T>): T? = firstOrNull { it !in reserved }

fun findInRangeForBits(bits: Int, filter: (ULong) -> Boolean): ULong = rangeForBits(bits).first(filter)
fun findInRangeForBitsOrNull(bits: Int, filter: (ULong) -> Boolean): ULong? = rangeForBits(bits).firstOrNull(filter)

fun findInRangeForBitsNotIn(bits: Int, reserved: Iterable<ULong>): ULong = rangeForBits(bits).firstNotIn(reserved)
fun findInRangeForBitsNotInOrNull(bits: Int, reserved: Iterable<ULong>): ULong? = rangeForBits(bits).firstNotInOrNull(reserved)
