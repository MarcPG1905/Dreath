package com.marcpg.dreath.world

import com.marcpg.dreath.scsdfwt.SDF
import com.marcpg.dreath.util.Seed
import kotlin.math.min

/**
 * A world with unique name, seed, features and modifications.
 * This will also be the main object for getting the SDF of a location.
 *
 * @param seed The seed used for random generation.
 * @param name The name of this world.
 *
 * @author MarcPG
 * @since 0.1.0
 */
class World(seed: Seed, name: String) : WorldContainerEntry(seed, name) {
    /** A list of modifications applied to this world. */
    private val mods: MutableList<SDF> = mutableListOf()

    /** A list of currently loaded chunks. */
    private val loadedChunks: MutableList<Chunk> = mutableListOf()

    /**
     * Gets this world's signed distance function for getting terrain and other things.
     */
    fun getSDF(): SDF = SDF { x, y, z ->
        var d = Double.POSITIVE_INFINITY

        for (feature in features) {
            val sdf = feature.getSDF()
            d = min(d, sdf(x, y, z))
        }
        return@SDF d
    }
}
