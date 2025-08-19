package com.marcpg.dreath.util

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

    /** This location's world, or null if none is set. */
    open var world: Any? = world
        protected set
}

/**
 * A mutable location with double-precision coordinates and a world, which can be nullable.
 * @author MarcPG
 * @since 0.1.0
 */
class MutableLocation(
    override var x: Double = 0.0,
    override var y: Double = 0.0,
    override var z: Double = 0.0,
    override var world: Any? = null
) : Location(x, y, z, world)
