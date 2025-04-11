package com.marcpg.modules.namegen

import com.marcpg.modules.algorithm.MarkovChain
import com.marcpg.util.ResourceUtil

object LocationNameGen {
    enum class LocationType(val nameLength: Int, val minPop: Int, val minBuildings: Int) {
        FAMILY(12, 1, 1),
        HAMLET(15, 10, 4),
        VILLAGE(12, 100, 20),
        TOWN(12, 2_000, 200),
        CITY(15, 100_000, 5_000),
        METROPOLIS(15, 1_000_000, 200_000),
        MEGALOPOLIS(12, 10_000_000, 1_000_000);

        val maxPop: Int = entries[this.ordinal + 1].minPop - 1
        val maxBuildings: Int = entries[this.ordinal + 1].minBuildings - 1
    }

    fun generate(type: LocationType): String {
        return MarkovChain(ResourceUtil.readLinesFromResource("dataset/location-names/${type.name.lowercase()}"), 2).generate(type.nameLength)
    }
}