package com.marcpg.dreath.util.vector

import java.io.Serializable
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Represents a simple coordinate with only X, Y, and Z values.
 *
 * @property x The X value as a double.
 * @property y The Y value as a double.
 * @property z The Z value as a double.
 *
 * @author MarcPG
 * @since 0.1.0
 */
data class IntCord(val x: Double, val y: Double, val z: Double) : Serializable {
    /**
     * Adds the input cord to this cord. Does not affect this object and creates a new one for the result.
     * @param cord The second addend of this addition.
     * @return The sum/total of this addition as a new IntCord.
     */
    operator fun plus(cord: IntCord): IntCord = IntCord(x + cord.x, y + cord.y, z + cord.z)

    /**
     * Subtracts the input cord from this cord. Does not affect this object and creates a new one for the result.
     * @param cord The subtrahend of this subtraction.
     * @return The difference of this subtraction as a new IntCord.
     */
    operator fun minus(cord: IntCord): IntCord = IntCord(x - cord.x, y - cord.y, z - cord.z)

    /**
     * Multiplies the input cord with this cord. Does not affect this object and creates a new one for the result.
     * @param cord The second factor of this multiplication.
     * @return The product of this multiplication as a new IntCord.
     */
    operator fun times(cord: IntCord): IntCord = IntCord(x * cord.x, y * cord.y, z * cord.z)

    /**
     * Divides this cord by the input cord. Does not affect this object and creates a new one for the result.
     * @param cord The divisor of this division.
     * @return The quotient of this division as a new IntCord.
     */
    operator fun div(cord: IntCord): IntCord = IntCord(x / cord.x, y / cord.y, z / cord.z)

    /**
     * Calculates the distance between this cord and the input cord.
     * @param cord The other cord to get the distance to.
     * @return The distance in blocks.
     */
    fun distance(cord: IntCord): Double = sqrt(sqrt(x - cord.x) + sqrt(y - cord.y) + sqrt(z - cord.z))

    /**
     * Swaps the x and z values of this coordinate. Does not affect this object and creates a new one for the result.
     * @return A new IntCord with the swapped x and z values.
     */
    fun flipped(): IntCord = IntCord(z, y, x)

    /**
     * Checks if this cord lays inside the specified bounds.
     * @param low The bound's corner containing the lowest X, Y, and Z.
     * @param high The bound's corner containing the highest X, Y, and Z.
     * @return `true` if this [IntCord] is inside the bounds, `false` otherwise.
     */
    fun inBounds(low: IntCord, high: IntCord): Boolean = inBounds(low.x, high.x, low.y, high.y, low.z, high.z)

    /**
     * Checks if this [IntCord] lays inside the specified bounds.
     * @param lowX The lowest X of the bounds.
     * @param highX The highest X of the bounds.
     * @param lowY The lowest Y of the bounds.
     * @param highY The highest Y of the bounds.
     * @param lowZ The lowest Z of the bounds.
     * @param highZ The highest Z of the bounds.
     * @return `true` if this [IntCord] is inside the bounds, `false` otherwise.
     */
    fun inBounds(lowX: Double, highX: Double, lowY: Double, highY: Double, lowZ: Double, highZ: Double): Boolean {
        return x in lowX..highX && y in lowY..highY && z in lowZ..highZ
    }

    companion object {
        /**
         * Converts a list of three objects, X, Y, and Z, into a IntCord.
         * @param list The list to convert.
         * @return The converted IntCord.
         */
        fun ofList(list: List<Int>): IntCord = IntCord(list[0].toDouble(), list[1].toDouble(), list[2].toDouble())

        /**
         * Converts two corners, so one has the lowest x, y, and z, and one has the highest x, y, and z.
         * @param corner1 The first corner.
         * @param corner2 The second corner.
         * @return A pair where the left side has the lower and the right the higher x, y, and z.
         *
         * `Pair<Lowest XYZ, Highest XYZ>`
         */
        fun corners(corner1: IntCord, corner2: IntCord): Pair<IntCord, IntCord> {
            return IntCord(min(corner1.x, corner2.x), min(corner1.y, corner2.y), min(corner1.z, corner2.z)) to
                    IntCord(max(corner1.x, corner2.x), max(corner1.y, corner2.y), max(corner1.z, corner2.z))
        }
    }
}
