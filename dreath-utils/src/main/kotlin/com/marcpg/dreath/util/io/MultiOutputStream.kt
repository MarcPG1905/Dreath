package com.marcpg.dreath.util.io

import java.io.OutputStream

/**
 * Represents an output stream which instead of writing something directly, just forwards all input to a list of other output streams.
 *
 * @param outs The output streams to forward all input to.
 *
 * @author MarcPG
 * @since 0.1.0
 */
class MultiOutputStream(private val outs: MutableList<OutputStream> = mutableListOf()) : OutputStream() {
    override fun write(b: Int) = outs.forEach { it.write(b) }
    override fun write(b: ByteArray) = outs.forEach { it.write(b) }
    override fun write(b: ByteArray, off: Int, len: Int) = outs.forEach { it.write(b, off, len) }
    override fun flush() = outs.forEach { it.flush() }
    override fun close() = outs.forEach { it.close() }

    /** Adds a new output streams to the list of output streams. */
    fun addOutputStream(outputStream: OutputStream) { outs.add(outputStream) }

    /**
     * Adds a new output streams to the list of output streams.
     *
     * @param outputStream The output stream to remove, if in the list.
     * @return `true` if the stream was removed, `false` if it was not in the list.
     */
    fun removeOutputStream(outputStream: OutputStream) = outs.remove(outputStream)
}
