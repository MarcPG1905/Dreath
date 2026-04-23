@file:Suppress("NOTHING_TO_INLINE")

package com.marcpg.dreath.util.vector

import com.marcpg.dreath.world.World
import com.marcpg.libpg.storing.Cord
import kotlin.math.sqrt

/**
 * A location with double-precision coordinates and a world, which can be nullable.
 *
 * @property x This location's X-coordinate in double-precision.
 * @property y This location's Y-coordinate in double-precision.
 * @property z This location's Z-coordinate in double-precision.
 * @property world This location's world, or null if none is set.
 *
 * @author MarcPG
 * @since 0.1.0
 */
open class Location(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, world: World? = null) {
    companion object {
        /**
         * A fake location at P(0|0|0) and the world set to `null`.
         * The same as constructing a new [Location] without any parameters.
         */
        val DUMMY = Location()
    }

    open var x: Double = x
        protected set

    open var y: Double = y
        protected set

    open var z: Double = z
        protected set

    open var world: World? = world
        protected set

    /** Convert this location with a world to a simple coordinate from LibPG. */
    fun toCord() = Cord(x, y, z)

    /** Converts this implementation of a location to the immutable base type. */
    fun toLocation(): Location = Location(x, y, z, world)

    /** Converts this implementation of a location to the mutable subtype. */
    fun toMutableLocation(): MutableLocation = MutableLocation(x, y, z, world)

    /**
     * Adds the input location to this location. Does not affect this object and creates a new one for the result.
     * @param other The second addend of this addition.
     * @return The sum/total of this addition as a new (Mutable)Location.
     */
    open operator fun plus(other: Location): Location = plus(other.x, other.y, other.z)

    /**
     * Adds the input values to this location. Does not affect this object and creates a new one for the result.
     *
     * Default values for x, y, and z are `0.0`, making the result not change in that axis.
     * @return The sum/total of this addition as a new (Mutable)Location.
     */
    open fun plus(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Location = Location(this.x + x, this.y + y, this.z + z, world)

    /**
     * Subtracts the input location from this location. Does not affect this object and creates a new one for the result.
     * @param other The subtrahend of this subtraction.
     * @return The difference of this subtraction as a new (Mutable)Location.
     */
    open operator fun minus(other: Location): Location = minus(other.x, other.y, other.z)

    /**
     * Subtracts the input values from this location. Does not affect this object and creates a new one for the result.
     *
     * Default values for x, y, and z are `0.0`, making the result not change in that axis.
     * @return The difference of this subtraction as a new (Mutable)Location.
     */
    open fun minus(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Location = Location(this.x - x, this.y - y, this.z - z, world)

    /**
     * Multiplies the input location with this location. Does not affect this object and creates a new one for the result.
     * @param other The second factor of this multiplication.
     * @return The product of this multiplication as a new (Mutable)Location.
     */
    open operator fun times(other: Location): Location = times(other.x, other.y, other.z)

    /**
     * Multiplies the input values with this location. Does not affect this object and creates a new one for the result.
     *
     * Default values for x, y, and z are `1.0`, making the result not change in that axis.
     * @return The product of this multiplication as a new (Mutable)Location.
     */
    open fun times(x: Double = 1.0, y: Double = 1.0, z: Double = 1.0): Location = Location(this.x * x, this.y * y, this.z * z, world)

    /**
     * Divides this location by the input location. Does not affect this object and creates a new one for the result.
     * @param other The divisor of this division.
     * @return The quotient of this division as a new (Mutable)Location.
     */
    open operator fun div(other: Location): Location = div(other.x, other.y, other.z)

    /**
     * Divides this location by the input values. Does not affect this object and creates a new one for the result.
     *
     * Default values for x, y, and z are `1.0`, making the result not change in that axis.
     * @return The quotient of this division as a new (Mutable)Location.
     */
    open fun div(x: Double = 1.0, y: Double = 1.0, z: Double = 1.0): Location = Location(this.x / x, this.y / y, this.z / z, world)

    /**
     * Calculates the distance between this location and the input location.
     * @param other The other location to get the distance to.
     * @return The distance in meters.
     */
    fun distance(other: Location): Double {
        require(this.world == other.world) { "Locations must be in same world" }
        return sqrt(sqrt(x - other.x) + sqrt(y - other.y) + sqrt(z - other.z))
    }

    /**
     * Checks if this location lays inside the specified bounds.
     * @param low The bound's corner containing the lowest X, Y, and Z.
     * @param high The bound's corner containing the highest X, Y, and Z.
     * @return `true` if this location is inside the bounds, `false` otherwise.
     */
    fun inBounds(low: Location, high: Location): Boolean {
        require(low.world == high.world) { "Low and high bounds must be in same world" }
        return this.world == low.world && inBounds(low.x, high.x, low.y, high.y, low.z, high.z)
    }

    /**
     * Checks if this location lays inside the specified bounds.
     * @param lowX The lowest X of the bounds.
     * @param highX The highest X of the bounds.
     * @param lowY The lowest Y of the bounds.
     * @param highY The highest Y of the bounds.
     * @param lowZ The lowest Z of the bounds.
     * @param highZ The highest Z of the bounds.
     * @return `true` if this location is inside the bounds, `false` otherwise.
     */
    fun inBounds(lowX: Double, highX: Double, lowY: Double, highY: Double, lowZ: Double, highZ: Double): Boolean {
        return x in lowX..highX && y in lowY..highY && z in lowZ..highZ
    }
}

/**
 * A mutable location with double-precision coordinates and a world, which can be nullable.
 * @author MarcPG
 * @since 0.1.0
 */
@Suppress("RedundantSetter")
class MutableLocation(
    x: Double = 0.0,
    y: Double = 0.0,
    z: Double = 0.0,
    world: World? = null
) : Location(x, y, z, world) {
    override var x: Double = x
        public set
    override var y: Double = y
        public set
    override var z: Double = z
        public set
    override var world: World? = world
        public set

    override operator fun plus(other: Location): MutableLocation = plus(other.x, other.y, other.z)
    override fun plus(x: Double, y: Double, z: Double): MutableLocation = MutableLocation(this.x + x, this.y + y, this.z + z, world)

    override operator fun minus(other: Location): MutableLocation = minus(other.x, other.y, other.z)
    override fun minus(x: Double, y: Double, z: Double): MutableLocation = MutableLocation(this.x - x, this.y - y, this.z - z, world)

    override operator fun times(other: Location): MutableLocation = times(other.x, other.y, other.z)
    override fun times(x: Double, y: Double, z: Double): MutableLocation = MutableLocation(this.x * x, this.y * y, this.z * z, world)

    override operator fun div(other: Location): MutableLocation = div(other.x, other.y, other.z)
    override fun div(x: Double, y: Double, z: Double): MutableLocation = MutableLocation(this.x / x, this.y / y, this.z / z, world)

    operator fun plusAssign(other: Location) {
        x += other.x
        y += other.y
        z += other.z
    }

    operator fun minusAssign(other: Location) {
        x -= other.x
        y -= other.y
        z -= other.z
    }

    operator fun timesAssign(other: Location) {
        x *= other.x
        y *= other.y
        z *= other.z
    }

    operator fun divAssign(other: Location) {
        x /= other.x
        y /= other.y
        z /= other.z
    }
}

/** Returns a new immutable location of the three axis and the world. */
fun locationOf(x: Double, y: Double, z: Double, world: World? = null) = Location(x, y, z, world)

/** Returns a new mutable location of the three axis and the world. */
fun mutableLocationOf(x: Double, y: Double, z: Double, world: World? = null) = MutableLocation(x, y, z, world)

/** Convert this simple coordinate from LibPG and the provided world to an immutable location. */
fun Cord.toLocation(world: World? = null) = Location(x, y, z, world)

/** Convert this simple coordinate from LibPG and the provided world to a mutable location. */
fun Cord.toMutableLocation(world: World? = null) = MutableLocation(x, y, z, world)
