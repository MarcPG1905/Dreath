package com.marcpg.dreath.gameobj

import com.marcpg.dreath.util.vector.Location
import com.marcpg.dreath.util.vector.MutableLocation
import com.marcpg.dreath.util.vector.vecFOf
import org.joml.Vector3f

/**
 * A physical object in the game which has a potion in any world.
 */
interface PhysicalObj : GameObj {
    /**
     * The relative location of this object, relative to the [owner].
     */
    val location: MutableLocation

    /**
     * The relative velocity of this object, relative to the [owner].
     */
    val velocity: Vector3f
}

/**
 * The absolute location of this object.
 *
 * If the owner is not a physical object, this just uses the relative [PhysicalObj.location].
 */
val PhysicalObj.absoluteLocation: Location
    get() = (owner as? PhysicalObj)?.absoluteLocation?.plus(location) ?: location

/**
 * The absolute velocity of this object.
 *
 * If the owner is not a physical object, this just uses the relative [PhysicalObj.velocity].
 */
val PhysicalObj.absoluteVelocity: Vector3f
    get() = (owner as? PhysicalObj)?.absoluteVelocity?.add(velocity, vecFOf(0f, 0f, 0f)) ?: velocity
