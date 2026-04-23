package com.marcpg.dreath.world

import com.marcpg.dreath.scsdfwt.SDF
import com.marcpg.dreath.util.vector.IntCord
import com.marcpg.dreath.world.feature.FeatureHolder

/**
 * A chunk of the world, separated for performance reasons and to isolate features.
 *
 * @property world This chunk's owning world.
 * @property cord This chunk's chunk-coordinate, so world-coordinate (150|0|50) would be chunk-coordinate (3|0|1).
 * @property mods A list of modifications applied to this chunk.
 *
 * @author MarcPG
 * @since 0.1.0
 */
class Chunk(val world: World, val cord: IntCord) : FeatureHolder() {
    private val mods: MutableList<SDF> = mutableListOf()
}
