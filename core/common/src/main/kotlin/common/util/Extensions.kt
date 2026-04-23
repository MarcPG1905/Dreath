package common.util

import kotlin.time.DurationUnit
import kotlin.time.TimeMark

fun TimeMark.elapsedMs(): String {
    val elapsed = elapsedNow()
    return elapsed.toString(if (elapsed.inWholeMilliseconds < 1000) {
        DurationUnit.MILLISECONDS
    } else if (elapsed.inWholeSeconds < 60) {
        DurationUnit.SECONDS
    } else {
        DurationUnit.MINUTES
    }, 1)
}

val Any.javaClassLoader: ClassLoader
    get() = this::class.java.classLoader
