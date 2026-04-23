package engine

import com.marcpg.dreath.util.registry.Registrar
import com.marcpg.dreath.util.registry.RegistrarType
import com.marcpg.dreath.world.feature.Feature

object FeatureRegistrar : Registrar<Feature> {
    override val type: RegistrarType<Feature> = RegistrarType.FEATURES

    private val loaded = mutableListOf<Feature>()

    override fun register(instance: Feature) {
        require(instance.name !in loaded.map { it.name }) { "Feature with name '${instance.name}' is already registered" }

        loaded += instance
    }

    override fun loaded(): List<Feature> = loaded.toList() // Only return a copy, never the actual mutable list.
}
