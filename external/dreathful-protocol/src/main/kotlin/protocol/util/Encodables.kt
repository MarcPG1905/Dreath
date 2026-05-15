package protocol.util

import java.nio.ByteBuffer

/**
 * Anything that can be encoded with a simple encode() method.
 */
interface Encodable {
    fun encode(): ByteArray
}

/**
 * Anything that can be encoded with a simple encode() method.
 */
interface DecodableObj<T> {
    fun decode(buffer: ByteBuffer): T
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
abstract class RawDataDecodableObj<T>(vararg segments: Int) : DecodableObj<T> {
    open val dataFormat: RawDataFormat = RawDataFormat(segments.toList())

    override fun decode(buffer: ByteBuffer): T = decode(dataFormat.decode(buffer))

    abstract fun decode(parts: List<ULong>): T
}