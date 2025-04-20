package com.marcpg.common

import com.marcpg.common.util.config.Settings
import com.marcpg.dreath.log.DreathLogger
import java.nio.file.Path
import kotlin.reflect.full.primaryConstructor
import kotlin.time.TimeMark

abstract class EnvironmentClass {
    abstract val dir: Path
    abstract val log: DreathLogger
    abstract val settings: Settings

    abstract fun run(start: TimeMark)
    open fun end() {
        settings.save()
    }
}

enum class Environment(val classPath: String?, val commandArgPath: String?) {
    CLIENT("com.marcpg.client.Client", "com.marcpg.client.ClientCommandArguments"),
    SERVER("com.marcpg.server.Server", "com.marcpg.server.ServerCommandArguments"),
    UNKNOWN(null, "com.marcpg.common.CommandArguments");

    fun instance(): EnvironmentClass? {
        return Class.forName(classPath ?: return null)?.kotlin?.objectInstance as? EnvironmentClass
    }

    fun commandArguments(): CommandArguments? {
        return Class.forName(commandArgPath ?: return null)?.kotlin?.primaryConstructor?.call() as? CommandArguments
    }

    companion object {
        fun current(): Environment {
            for (entry in entries) {
                try {
                    Class.forName(entry.classPath, false, Game::class.java.classLoader)
                    return entry
                } catch (ignored: Exception) {}
            }
            return UNKNOWN
        }
    }
}
