package com.marcpg.dreath.util

import java.io.OutputStream

class MultiOutputStream(private val outs: MutableList<OutputStream> = mutableListOf()) : OutputStream() {
    override fun write(b: Int) = outs.forEach { it.write(b) }
    override fun write(b: ByteArray) = outs.forEach { it.write(b) }
    override fun write(b: ByteArray, off: Int, len: Int) = outs.forEach { it.write(b, off, len) }
    override fun flush() = outs.forEach { it.flush() }
    override fun close() = outs.forEach { it.close() }

    fun addOutputStream(outputStream: OutputStream) = outs.add(outputStream)
}
