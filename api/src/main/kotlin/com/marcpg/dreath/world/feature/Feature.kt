package com.marcpg.dreath.world.feature

import com.marcpg.dreath.scsdfwt.SDF
import com.marcpg.dreath.util.Seed

/**
 * Represents some kind of feature that can be applied to worlds/chunks via a [FeatureHolder].
 * This can be anything from mountains, to water, to corrosion, or separate entities.
 *
 * @property name This feature's name for debugging purposes.
 *
 * @author MarcPG
 * @since 0.1.0
 */
interface Feature {
    val name: String

    /**
     * The seed used for this feature if it is required.
     * This is usually overridden inside the extending class's constructor.
     */
    val seed: Seed
        get() = Seed.ZERO

    /** Gets the [SDF] for this feature, which will be merged with the other features that were applied before. */
    fun getSDF(): SDF

    /**
     * GLSL function name to use in the combined GPU SDF shader.
     * Should be unique per feature **in this mod** to avoid naming collisions.
     */
    val glslFuncName: String
        get() = name.replace("\\s+".toRegex(), "_")

    /** Returns the GLSL implementation of this feature for runtime shader compilation. */
    fun getShaderSDF(): String
}
