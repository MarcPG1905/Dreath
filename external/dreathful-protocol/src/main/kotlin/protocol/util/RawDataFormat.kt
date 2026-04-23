package protocol.util

import java.nio.ByteBuffer
import kotlin.math.ceil

/**
 * Represents a format for packing raw data into a [ByteArray] and take up as least space as possible.
 * This will usually only need a few CPU clock cycles for encoding/decoding data, depending on the amount of segments.
 */
class RawDataFormat(
    /** A list of all segments with their bit width, so how many bits of the data they take up. */
    val segments: List<Int>
) {
    constructor(vararg segments: Int) : this(segments.toList())

    private val dataLength: Int = segments.sum()

    /** Bytes required for all segments. */
    val byteLength: Int = ceil(dataLength / 8.0).toInt()

    /**
     * Encodes an array of data into a [ByteArray] using this specified format.
     *
     * ### Example
     *
     * Output data might look like this, if the output was only 32 bits.
     * The numbers here are the indexes of the input [data]:
     * ```
     * ---- ---- --00 1111 2223 4455 6667 8999
     * ```
     * @see decode
     */
    fun encode(vararg data: ULong): ByteArray = encode(data.toList())

    /**
     * Encodes a list of data into a [ByteArray] using this specified format.
     *
     * ### Example
     *
     * Output data might look like this, if the output was only 32 bits.
     * The numbers here are the indexes of the input [data]:
     * ```
     * ---- ---- --00 1111 2223 4455 6667 8999
     * ```
     * @see decode
     */
    fun encode(data: List<ULong>): ByteArray {
        require(data.size == segments.size) { "Data must match the number of segments" }

        val bits = ByteArray(byteLength)
        var bitPos = byteLength * 8 - dataLength

        for ((i, width) in segments.withIndex()) {
            val value = data[i]

            for (b in width - 1 downTo 0) {
                val bit = ((value shr b) and 1uL).toInt()
                val byteIndex = bitPos / 8
                val bitIndex = 7 - (bitPos % 8)
                bits[byteIndex] = (bits[byteIndex].toInt() or (bit shl bitIndex)).toByte()
                bitPos++
            }
        }
        return bits
    }

    /**
     * Decodes a single [ULong] into a list of data using this specified format.
     * This requires the same format as encoded by [encode].
     * @see encode
     */
    fun decode(buffer: ByteBuffer): List<ULong> {
        require(buffer.remaining() >= byteLength) { "Buffer does not contain enough data (expected $byteLength bytes, got ${buffer.remaining()})" }

        val list = kotlin.collections.ArrayList<ULong>(segments.size)

        val baseOffset = buffer.position()
        var bitPos = byteLength * 8 - dataLength

        for (width in segments) {
            var value = 0uL
            for (b in width - 1 downTo 0) {
                val byteIndex = baseOffset + (bitPos / 8)
                val bitIndex = 7 - (bitPos % 8)

                val byte = buffer.get(byteIndex).toInt()
                value = value or (((byte ushr bitIndex) and 1).toULong() shl b)
                bitPos++
            }
            list.add(value)
        }

        buffer.position(baseOffset + byteLength)

        return list
    }
}

/** @see RawDataFormat.encode */
fun List<ULong>.encode(format: RawDataFormat): ByteArray = format.encode(this)

/** @see RawDataFormat.decode */
fun ByteBuffer.decode(format: RawDataFormat): List<ULong> = format.decode(this)

/**
 * Anything that can be encoded with a simple encode() method.
 */
interface RawDataEncodable {
    fun encode(): ByteArray
}

/**
 * Anything that can be decoded with a simple decode() method and has a [RawDataFormat] attached.
 *
 * This should be implemented in companion objects.
 * An example implementation could look like this:
 * ```kt
 * companion object : RawDataDecodableObj<ObjectType>(/*...*/) {
 *     override fun decode(data: ULong) : ObjectType {
 *         val d = dataFormat.decode(data)
 *         return ObjectType(d[0].toInt(), ...)
 *     }
 * }
 * ```
 */
abstract class RawDataDecodableObj<T>(vararg segments: Int) {
    open val dataFormat: RawDataFormat = RawDataFormat(segments.toList())

    abstract fun decode(buffer: ByteBuffer): T
}
