package com.marcpg.dreath.util

import com.marcpg.libpg.util.toBoolean

/**
 * Uses KtLibPG's `Number.toBoolean()` to convert this into a boolean.
 * @return `true` if this is 1, `false` otherwise.
 */
fun ULong.toBoolean(): Boolean = toLong().toBoolean()

/**
 * Uses KtLibPG's `Number.toBoolean()` to convert this into a boolean.
 * @return `true` if this is 1, `false` otherwise.
 */
fun UInt.toBoolean(): Boolean = toInt().toBoolean()

/**
 * Uses KtLibPG's `Number.toBoolean()` to convert this into a boolean.
 * @return `true` if this is 1, `false` otherwise.
 */
fun UShort.toBoolean(): Boolean = toShort().toBoolean()

/**
 * Uses KtLibPG's `Number.toBoolean()` to convert this into a boolean.
 * @return `true` if this is 1, `false` otherwise.
 */
fun UByte.toBoolean(): Boolean = toByte().toBoolean()

/**
 * Converts this UInt to a 4-bit ByteArray in big-endian order, meaning that the most significant byte is at index 0,
 * and the least significant byte is at index 3.
 * @return A [ByteArray] of exactly 4 bytes representing this value in big-endian byte order.
 */
fun UInt.toByteArray(): ByteArray = byteArrayOf(
    (this shr 24).toByte(),
    (this shr 16).toByte(),
    (this shr 8).toByte(),
    this.toByte()
)

/**
 * Converts this ULong to an 8-bit ByteArray in big-endian order, meaning that the most significant byte is at index 0,
 * and the least significant byte is at index 7.
 * @return A [ByteArray] of exactly 8 bytes representing this value in big-endian byte order.
 */
fun ULong.toByteArray(): ByteArray = byteArrayOf(
    (this shr 56).toByte(),
    (this shr 48).toByte(),
    (this shr 40).toByte(),
    (this shr 32).toByte(),
    (this shr 24).toByte(),
    (this shr 16).toByte(),
    (this shr 8).toByte(),
    this.toByte()
)

/**
 * Converts this 8-bit ByteArray to a UInt in big-endian order, meaning that the most significant byte is at index 0,
 * and the least significant byte is at index 3.
 * @return A [UInt] constructed from this ByteArray.
 */
fun ByteArray.toUInt(): UInt {
    val b0 = this[0].toInt() and 0xFF
    val b1 = this[1].toInt() and 0xFF
    val b2 = this[2].toInt() and 0xFF
    val b3 = this[3].toInt() and 0xFF

    return ((b0 shl 24) or
            (b1 shl 16) or
            (b2 shl 8) or
            b3).toUInt()
}

/**
 * Converts this 8-bit ByteArray to a ULong in big-endian order, meaning that the most significant byte is at index 0,
 * and the least significant byte is at index 7.
 * @return A [ULong] constructed from this ByteArray.
 */
fun ByteArray.toULong(): ULong {
    val b0 = this[0].toLong() and 0xFFL
    val b1 = this[1].toLong() and 0xFFL
    val b2 = this[2].toLong() and 0xFFL
    val b3 = this[3].toLong() and 0xFFL
    val b4 = this[4].toLong() and 0xFFL
    val b5 = this[5].toLong() and 0xFFL
    val b6 = this[6].toLong() and 0xFFL
    val b7 = this[7].toLong() and 0xFFL

    return ((b0 shl 56) or
            (b1 shl 48) or
            (b2 shl 40) or
            (b3 shl 32) or
            (b4 shl 24) or
            (b5 shl 16) or
            (b6 shl 8) or
            b7).toULong()
}
