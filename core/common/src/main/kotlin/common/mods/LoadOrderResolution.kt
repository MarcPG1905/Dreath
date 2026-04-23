package common.mods

import com.marcpg.dreath.ModInfo
import com.marcpg.dreath.util.Constants

object LoadOrderResolution {
    fun resolve(mods: Set<ModInfo>): List<ModInfo> {
        val modsById = indexByIds(mods)
        val validMods = checkDependencies(mods, modsById)

        val graph = createDependencyGraph(validMods, modsById)
        checkDependencyCycles(validMods, graph)
        val result = topologicalSort(graph)

        if (result.size != mods.size)
            throw IllegalStateException("Could not resolve all mods. Graph may have a cycle.")
        return result
    }

    fun Map<String, List<ModInfo>>.getMatching(dep: ModInfo.Dependencies.Dependency): ModInfo? = get(dep.id)?.firstOrNull { it.version >= dep.minVersion }

    fun indexByIds(mods: Set<ModInfo>) = mods.groupBy { it.id }.mapValues { entry -> entry.value.sortedByDescending { it.version } }

    fun checkDependencies(mods: Set<ModInfo>, modsById: Map<String, List<ModInfo>>): List<ModInfo> {
        val currentDreathVersion = Constants.VERSION.toSimpleVersion()
        return mods.filter { mod -> currentDreathVersion >= mod.dependencies.dreath && mod.dependencies.external.all { if (it.required) modsById.getMatching(it) != null else true } }
    }

    fun createDependencyGraph(mods: List<ModInfo>, modsById: Map<String, List<ModInfo>>): Map<ModInfo, Set<ModInfo>> {
        val graph = mutableMapOf<ModInfo, MutableSet<ModInfo>>()
        mods.forEach { graph[it] = mutableSetOf() }

        mods.forEach { mod -> mod.dependencies.external.forEach {
            val target = modsById.getMatching(it)
            if (target != null) {
                when (it.load) {
                    ModInfo.Dependencies.Dependency.LoadOrder.BEFORE -> graph[mod]?.add(target)

                    ModInfo.Dependencies.Dependency.LoadOrder.AFTER, // Both AFTER and AUTO use dependency first.
                    ModInfo.Dependencies.Dependency.LoadOrder.AUTO-> graph[target]?.add(mod)
                }
            }
        } }

        return graph
    }

    fun checkDependencyCycles(mods: List<ModInfo>, graph: Map<ModInfo, Set<ModInfo>>) {
        val visited = mutableSetOf<ModInfo>()
        val visiting = mutableSetOf<ModInfo>()

        fun hasCycle(node: ModInfo): Boolean {
            if (node in visiting) return true
            if (node in visited) return false
            visiting.add(node)
            if (graph[node]?.any { hasCycle(it) } == true) return true
            visiting.remove(node)
            visited.add(node)
            return false
        }

        if (mods.any { hasCycle(it) })
            throw IllegalStateException("Load order cycle detected in dependencies")
    }

    fun <T> topologicalSort(graph: Map<T, Set<T>>): List<T> {
        val indegree = mutableMapOf<T, Int>().withDefault { 0 }
        graph.forEach { (_, deps) ->
            deps.forEach { indegree[it] = indegree.getValue(it) + 1 }
        }

        val queue = ArrayDeque<T>()
        indegree.forEach { (mod, deg) -> if (deg == 0) queue.add(mod) }

        val result = mutableListOf<T>()
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            result.add(current)
            graph[current]?.forEach { neighbor ->
                indegree[neighbor] = indegree.getValue(neighbor) - 1
                if (indegree.getValue(neighbor) == 0) queue.add(neighbor)
            }
        }
        return result
    }
}
