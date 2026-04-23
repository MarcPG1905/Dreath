@file:Suppress("NOTHING_TO_INLINE")

package com.marcpg.dreath.util.vector

import com.marcpg.dreath.world.World
import com.marcpg.libpg.storing.Cord
import org.joml.*

// Precise double (64-bit) vectors

/** Construct a Vector4d from the set `x`, `y`, `z`, and `w` components. */
fun vecDOf(x: Double, y: Double, z: Double, w: Double) = Vector4d(x, y, z, w)

/** Construct a Vector3d from the set `x`, `y`, and `z` components. */
fun vecDOf(x: Double, y: Double, z: Double) = Vector3d(x, y, z)

/** Construct a Vector2d from the set `x`, and `y` components. */
fun vecDOf(x: Double, y: Double) = Vector2d(x, y)


/** Convert this simple coordinate from LibPG to a Vector3d. */
fun Cord.toVecD() = Vector3d(x, y, z)

/** Convert this location with a world to a simple Vector3d. */
fun Location.toVecD() = Vector3d(x, y, z)

/** Convert this Vector3f (32-bit) to a Vector3d (64-bit). */
fun Vector3f.toVecD() = vecDOf(x.toDouble(), y.toDouble(), z.toDouble())


/** Create a copy of this vector with normalized values. Same as manually typing `normalize(Vector3d())`. */
inline fun Vector3d.normalizedCopy(): Vector3d = normalize(Vector3d())

/** Create a copy of this vector. Same as manually typing `vecDOf(it.x, it.y, it.z)`. */
inline fun Vector3d.copy(): Vector3d = vecDOf(x, y, z)


/** Convert this Vector3d to an immutable location with the (optionally) provided world. */
fun Vector3d.toLocation(world: World? = null) = Location(x, y, z, world)

/** Convert this Vector3d to a mutable location with the (optionally) provided world. */
fun Vector3d.toMutableLocation(world: World? = null) = MutableLocation(x, y, z, world)

/** Convert this Vector3d to a simple coordinate from LibPG. */
fun Vector3d.toCord() = Cord(x, y, z)

// Fast float (32-bit) vectors


/** Construct a Vector4f from the set `x`, `y`, `z`, and `w` components. */
fun vecFOf(x: Float, y: Float, z: Float, w: Float) = Vector4f(x, y, z, w)

/** Construct a Vector3f from the set `x`, `y`, and `z` components. */
fun vecFOf(x: Float, y: Float, z: Float) = Vector3f(x, y, z)

/** Construct a Vector2f from the set `x`, and `y` components. */
fun vecFOf(x: Float, y: Float) = Vector2f(x, y)


/** Convert this simple coordinate from LibPG to a Vector3f. */
fun Cord.toVecF() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

/** Convert this location with a world to a simple Vector3f. */
fun Location.toVecF() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

/** Convert this Vector3d (64-bit) to a Vector3f (32-bit). */
fun Vector3d.toVecF() = vecFOf(x.toFloat(), y.toFloat(), z.toFloat())


/** Create a copy of this vector with normalized values. Same as manually typing `normalize(Vector3f())`. */
inline fun Vector3f.normalizedCopy(): Vector3f = normalize(Vector3f())

/** Create a copy of this vector. Same as manually typing `vecFOf(it.x, it.y, it.z)`. */
inline fun Vector3f.copy(): Vector3f = vecFOf(x, y, z)


/** Convert this Vector3f to an immutable location with the (optionally) provided world. */
fun Vector3f.toLocation(world: World? = null) = Location(x.toDouble(), y.toDouble(), z.toDouble(), world)

/** Convert this Vector3f to a mutable location with the (optionally) provided world. */
fun Vector3f.toMutableLocation(world: World? = null) = MutableLocation(x.toDouble(), y.toDouble(), z.toDouble(), world)

/** Convert this Vector3d to a simple coordinate from LibPG. */
fun Vector3f.toCord() = Cord(x.toDouble(), y.toDouble(), z.toDouble())
