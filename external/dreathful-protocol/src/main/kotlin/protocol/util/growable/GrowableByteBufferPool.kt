package protocol.util.growable

class GrowableByteBufferPool(private val defaultCapacity: Int) {
    companion object {
        /** A small default buffer pool for buffers with a default capacity of `16` bytes. */
        val Default_Small = GrowableByteBufferPool(16)

        /** A default buffer pool for buffers with a default capacity of `128` bytes. */
        val Default_Normal = GrowableByteBufferPool(128)

        /** A large default buffer pool for buffers with a default capacity of `512` bytes. */
        val Default_Large = GrowableByteBufferPool(512)

        /** A default buffer pool for buffers with a default capacity of the default max. MTU, which is `1452` bytes. */
        val Default_MTU = GrowableByteBufferPool(1452)
    }

    private val pool: ArrayDeque<GrowableByteBuffer> = ArrayDeque()

    fun acquire(): GrowableByteBuffer = pool.removeFirstOrNull() ?: GrowableByteBuffer(this, defaultCapacity)

    fun release(buffer: GrowableByteBuffer) {
        check(buffer !in pool) { "Buffer has previously been released" }

        buffer.clear()
        pool.addFirst(buffer)
    }

    inline fun <T> temporary(usage: (GrowableByteBuffer) -> T): T {
        val buffer = acquire()
        val result = usage(buffer)
        release(buffer)

        return result
    }

    inline fun asByteArray(setter: (GrowableByteBuffer) -> Unit): ByteArray = temporary {
        setter(it)
        it.toByteArray()
    }
}
