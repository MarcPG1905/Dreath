package com.marcpg.log

import java.io.OutputStream

class MultiOutputStream(private val out1: OutputStream, private val out2: OutputStream) : OutputStream() {
    override fun write(b: Int) {
        out1.write(b)
        out2.write(b)
    }

    override fun write(b: ByteArray) {
        out1.write(b)
        out2.write(b)
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        out1.write(b, off, len)
        out2.write(b, off, len)
    }

    override fun flush() {
        out1.flush()
        out2.flush()
    }

    override fun close() {
        out1.close()
        out2.close()
    }
}