package com.marcpg.dreath.scsdfwt

import com.marcpg.dreath.util.vector.Location
import kotlin.math.max
import kotlin.math.min

/**
 * # Signed Distance Function
 *
 * Represents an SDF (Signed Distance Function).
 *
 * This also has basic CSG (Constructive Solid Geometry) for doing more complex stuff.
 *
 * ## SDF Output
 *
 * An SDF is a function that returns the shortest distance to the surface of a shape based on a 3D point.
 * Output is always one of these:
 * - **Negative:** Inside the shape.
 * - **Zero:** On the exact surface of the shape.
 * - **Positive:** Outside the shape.
 */
fun interface SDF {
    /**
     * Cheks the signed distance at the provided 3D point.
     *
     * ## SDF Output
     *
     * An SDF is a function that returns the shortest distance to the surface of a shape based on a 3D point.
     * Output is always one of these:
     * - **Negative:** Inside the shape.
     * - **Zero:** On the exact surface of the shape.
     * - **Positive:** Outside the shape.
     */
    operator fun invoke(x: Double, y: Double, z: Double): Double

    operator fun invoke(x: Float, y: Float, z: Float): Float = invoke(x.toDouble(), y.toDouble(), z.toDouble()).toFloat()

    operator fun invoke(location: Location): Double = invoke(location.x, location.y, location.z)

    /**
     * Returns a new SDF representing the union of this SDF and an[other] one.
     * The union combines both shapes and returns the minimum distance.
     * @see smoothUnion
     */
    fun union(other: SDF) = SDF { x, y, z -> min(this(x, y, z), other(x, y, z)) }

    /**
     * Returns a new SDF representing the intersection of this SDF and an[other] one.
     * The intersection is points inside both shapes and returns the maximum distance.
     * @see smoothIntersection
     */
    fun intersection(other: SDF) = SDF { x, y, z -> max(this(x, y, z), other(x, y, z)) }

    /**
     * Returns a new SDF representing the difference of this SDF minus an[other] one.
     * The difference subtracts the [other] SDF from this one.
     * @see smoothDifference
     */
    fun difference(other: SDF) = SDF { x, y, z -> max(this(x, y, z), -other(x, y, z)) }

    /**
     * Returns a new SDF representing the smooth union of this SDF and an[other] one.
     * This blends the shapes softly to avoid sharp edges.
     * @see union
     */
    fun smoothUnion(other: SDF, k: Double) = SDF { x, y, z ->
        val d1 = this(x, y, z)
        val d2 = other(x, y, z)
        val h = ((1.0 + (d2 - d1) / k) * 0.5).coerceIn(0.0, 1.0)
        return@SDF lerp(d2, d1, h) - k * h * (1.0 - h)
    }

    /**
     * Returns a new SDF representing the smooth intersection of this SDF and an[other] one.
     * This blends the intersection softly to avoid sharp edges.
     * @see intersection
     */
    fun smoothIntersection(other: SDF, k: Double) = SDF { x, y, z ->
        val d1 = this(x, y, z)
        val d2 = other(x, y, z)
        val h = ((1.0 - (d2 - d1) / k) * 0.5).coerceIn(0.0, 1.0)
        return@SDF lerp(d2, d1, h) + k * h * (1.0 - h)
    }

    /**
     * Returns a new SDF representing the smooth difference of this SDF minus an[other] one.
     * This blends the subtraction softly to avoid sharp edges.
     * @see difference
     */
    fun smoothDifference(other: SDF, k: Double) = SDF { x, y, z ->
        val d1 = this(x, y, z)
        val d2 = other(x, y, z)
        val h = ((1.0 - (d2 + d1) / k) * 0.5).coerceIn(0.0, 1.0)
        return@SDF lerp(d1, -d2, h) + k * h * (1.0 - h)
    }

    /**
     * Returns a new SDF translated by the provided vector.
     * This only moves the shape and does not change its form.
     */
    fun translate(dx: Double, dy: Double, dz: Double) = SDF { x, y, z -> this(x - dx, y - dy, z - dz) }

    /**
     * Returns a new SDF scaled by the factor [s].
     * This shrinks or enlarges the shape proportionally.
     */
    fun scale(s: Double) = SDF { x, y, z -> this(x / s, y / s, z / s) * s }

    /**
     * Returns a new SDF offset by distance [d].
     * A positive distance inflates and a negative one deflates this shape.
     */
    fun offset(d: Double): SDF = SDF { x, y, z -> this(x, y, z) - d }

    /**
     * Returns a new SDF makes by the provided predicate.
     * If the predicate is false at a point, the distance will be [positive infinity][Double.POSITIVE_INFINITY], representing empty space.
     */
    fun mask(predicate: (Double, Double, Double) -> Boolean): SDF = SDF { x, y, z -> if (predicate(x, y, z)) this(x, y, z) else Double.POSITIVE_INFINITY }

    /**
     * Linearly interpolates between [a] and [b] by [t].
     */
    fun lerp(a: Double, b: Double, t: Double) = a * (1 - t) + b * t
}
