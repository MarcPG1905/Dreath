package com.marcpg.dreath.util

/**
 * A mutable location with double-precision coordinates and a world, which can be nullable.
 */
class MutableLocation(
    override var x: Double = 0.0,
    override var y: Double = 0.0,
    override var z: Double = 0.0,
    override var world: Any? = null
) : Location(x, y, z, world)