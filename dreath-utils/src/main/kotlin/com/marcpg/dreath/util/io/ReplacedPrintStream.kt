package com.marcpg.dreath.util.io

import java.io.PrintStream
import java.nio.charset.Charset

/**
 * An implementation of [PrintStream] which forwards all writes to the stream with the call of a custom [replacement] method.
 *
 * @author MarcPG
 * @since 0.1.0
 */
abstract class ReplacedPrintStream(actual: PrintStream) : PrintStream(actual) {
    private val charset: Charset = runCatching { actual.charset() }.getOrElse { Charset.defaultCharset() }

    /**
     * The replacement method.
     *
     * @param any Can be anything, as [println] for example has one version taking `Any?`.
     */
    abstract fun replacement(any: Any?)

    final override fun write(b: Int) = replacement(byteArrayOf(b.toByte()).toString(charset))
    final override fun write(buf: ByteArray, off: Int, len: Int) = replacement(if (len > 0) String(buf, off, len, charset) else null)

    final override fun println() {}

    final override fun println(x: Boolean) = replacement(x)
    final override fun println(x: Char) = replacement(x)
    final override fun println(x: Int) = replacement(x)
    final override fun println(x: Long) = replacement(x)
    final override fun println(x: Float) = replacement(x)
    final override fun println(x: Double) = replacement(x)
    final override fun println(x: CharArray) = replacement(x)
    final override fun println(x: String?) = replacement(x)
    final override fun println(x: Any?) = replacement(x)

    final override fun print(b: Boolean) = replacement(b)
    final override fun print(c: Char) = replacement(c)
    final override fun print(i: Int) = replacement(i)
    final override fun print(l: Long) = replacement(l)
    final override fun print(f: Float) = replacement(f)
    final override fun print(d: Double) = replacement(d)
    final override fun print(s: CharArray) = replacement(s)
    final override fun print(s: String?) = replacement(s)
    final override fun print(obj: Any?) = replacement(obj)
}
