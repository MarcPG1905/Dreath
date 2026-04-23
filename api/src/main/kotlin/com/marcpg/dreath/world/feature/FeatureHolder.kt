package com.marcpg.dreath.world.feature

/**
 * A feature holder that can have multiple features applied to it in a specific order.
 * @author MarcPG
 * @since 0.1.0
 */
abstract class FeatureHolder {
    /**
     * All features that are currently applied to this holder.
     *
     * This should always be ordered for a proper world generation!
     */
    protected val features: MutableList<Feature> = mutableListOf()

    /** Adds a new feature at the very end of the list. */
    fun addFeature(feature: Feature) { features += feature }

    /** Removes a specific feature from this feature holder. */
    fun removeFeature(feature: String): Boolean = features.removeAll { it.name == feature }

    /** Lists all features by their names ([Feature.name]). */
    fun listFeatures(feature: Feature): List<String> = features.map { it.name }
}
