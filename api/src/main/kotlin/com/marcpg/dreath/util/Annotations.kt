package com.marcpg.dreath.util

/**
 * A marker annotation for DSLs in Dreath.
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
internal annotation class DreathDsl
