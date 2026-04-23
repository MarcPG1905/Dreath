package common.util.io

import java.io.OutputStream
import java.io.PrintStream
import java.util.*

class FilteredOutputStream(out: OutputStream) : PrintStream(out) {
    companion object {
        val filters = mutableListOf<Regex>()
    }

    override fun print(x: String?) {
        if (x != null) {
            if (filters.any { !x.matches(it) }) return
        }
        super.print(x)
    }
    override fun println(x: String?) {
        if (x != null) {
            if (filters.any { !x.matches(it) }) return
        }
        super.println(x)
    }

    override fun print(x: CharArray) {
        val s = String(x)
        if (filters.any { !s.matches(it) }) return
        super.print(x)
    }
    override fun println(x: CharArray) {
        val s = String(x)
        if (filters.any { !s.matches(it) }) return
        super.println(x)
    }

    override fun print(x: Any?) {
        if (x != null) {
            val s = x.toString()
            if (filters.any { !s.matches(it) }) return
        }
        super.print(x)
    }
    override fun println(x: Any?) {
        if (x != null) {
            val s = x.toString()
            if (filters.any { !s.matches(it) }) return
        }
        super.println(x)
    }

    override fun printf(format: String, vararg args: Any?): PrintStream {
        if (filters.any { !format.matches(it) }) return this
        return super.printf(format, *args)
    }

    override fun printf(l: Locale?, format: String, vararg args: Any?): PrintStream {
        if (filters.any { !format.matches(it) }) return this
        return super.printf(l, format, *args)
    }
}
