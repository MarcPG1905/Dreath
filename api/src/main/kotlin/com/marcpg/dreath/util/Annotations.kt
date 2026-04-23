package com.marcpg.dreath.util

/**
 * A marker annotation for DSLs in Dreath.
 * @author MarcPG
 * @since 0.1.0
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
internal annotation class DreathDsl
