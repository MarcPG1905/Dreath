package protocol.util.growable

import java.nio.ByteBuffer
import kotlin.math.max

class GrowableByteBuffer internal constructor(val pool: GrowableByteBufferPool, initialCapacity: Int) {
    private var buffer = ByteBuffer.allocate(initialCapacity)

    private fun ensureRemaining(needed: Int) {
        if (buffer.remaining() >= needed) return

        val grown = ByteBuffer.allocate(max(buffer.capacity() * 2, buffer.position() + needed))
        buffer.flip()
        grown.put(buffer)
        buffer = grown
    }

    fun buffer(): ByteBuffer = buffer.flip()
    fun rawBuffer(): ByteBuffer = buffer

    fun clear() {
        buffer.clear()
    }

    fun release() = pool.release(this)

    fun toByteArray(): ByteArray = buffer.array()

    // Long Data

    fun putBytes(buffer: ByteBuffer): GrowableByteBuffer {
        ensureRemaining(1)
        buffer.put(buffer)
        return this
    }

    fun putBytes(array: ByteArray): GrowableByteBuffer {
        ensureRemaining(1)
        buffer.put(buffer)
        return this
    }

    // Single Primitives

    fun putByte(b: Byte): GrowableByteBuffer {
        ensureRemaining(1)
        buffer.put(b)
        return this
    }

    fun putChar(c: Char): GrowableByteBuffer {
        ensureRemaining(2)
        buffer.putChar(c)
        return this
    }

    fun putShort(s: Short): GrowableByteBuffer {
        ensureRemaining(2)
        buffer.putShort(s)
        return this
    }

    fun putInt(i: Int): GrowableByteBuffer {
        ensureRemaining(4)
        buffer.putInt(i)
        return this
    }

    fun putLong(l: Long): GrowableByteBuffer {
        ensureRemaining(8)
        buffer.putLong(l)
        return this
    }

    fun putFloat(f: Float): GrowableByteBuffer {
        ensureRemaining(4)
        buffer.putFloat(f)
        return this
    }

    fun putDouble(d: Double): GrowableByteBuffer {
        ensureRemaining(8)
        buffer.putDouble(d)
        return this
    }

    // Unsigned Equivalents of Primitives

    fun putUShort(b: UShort): GrowableByteBuffer = putShort(b.toShort())
    fun putUInt(b: UInt): GrowableByteBuffer = putInt(b.toInt())
    fun putULong(b: ULong): GrowableByteBuffer = putLong(b.toLong())
}
