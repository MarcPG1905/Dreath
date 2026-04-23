package com.marcpg.dreath.world

import com.marcpg.dreath.gameobj.GameObj
import com.marcpg.dreath.util.Seed
import com.marcpg.dreath.world.feature.FeatureHolder
import org.intellij.lang.annotations.Pattern

/**
 * A world container used for nesting multiple worlds.
 * @author MarcPG
 * @since 0.1.0
 *
 * @param sharedSeed The seed used for all worlds in this container.
 * @param containerName The name of this container.
 *
 * @property worlds A list of worlds contained in this container.
 *
 * @author MarcPG
 * @since 0.1.0
 */
open class WorldContainer(sharedSeed: Seed, containerName: String) : WorldContainerEntry(sharedSeed, containerName) {
    protected val worlds: MutableList<WorldContainerEntry> = mutableListOf()

    /** Adds a new world or world container to this container. */
    fun addWorld(world: WorldContainerEntry) {
        world.parentContainer = this
        worlds.add(world)
    }

    /** Removes a world or world container from this container. */
    fun removeWorld(world: WorldContainerEntry): Boolean {
        world.parentContainer = null
        return worlds.remove(world)
    }

    /** Lists all worlds or world containers inside this container. */
    fun listEntries(): List<WorldContainerEntry> = worlds.toList()

    /** Lists all actual worlds inside this container. */
    fun listWorlds(): List<World> = worlds.filterIsInstance<World>()
}

/**
 * Anything that can be added to a [WorldContainer]. Usually either a [World] or another [WorldContainer] for nesting.
 *
 * @param seed The seed used for this world or world container.
 * @param name The name of this world or world container.
 *
 * @property parentContainer The parent container of this entry. Can be set manually if null at the time of setting.
 * @property hasParentContainer Whether this entry has a parent container or not.
 *
 * @author MarcPG
 * @since 0.1.0
 */
abstract class WorldContainerEntry(val seed: Seed, @param:Pattern("^[a-zA-Z0-9-]{2,}$") val name: String) : FeatureHolder(), GameObj {
    var parentContainer: WorldContainer? = null
        set(value) {
            if (value != null && field != null)
                error("World container entry already has a parent container")
            field = value
        }

    val hasParentContainer: Boolean
        get() = parentContainer != null

    override val owner: GameObj? = parentContainer
}
