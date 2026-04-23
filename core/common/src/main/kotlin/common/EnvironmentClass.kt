package common

import com.marcpg.dreath.log.LoggerOwnerImpl
import common.util.config.GameConfig
import common.util.javaClassLoader
import java.nio.file.Path
import kotlin.reflect.full.primaryConstructor
import kotlin.time.TimeMark

abstract class EnvironmentClass<T : GameConfig> : LoggerOwnerImpl() {
    abstract val dir: Path
    abstract val config: T

    open fun run(start: TimeMark) { config.init() }
    open fun end() { config.save() }
}

enum class Environment(val classPath: String?, val commandArgPath: String?) {
    CLIENT("client.Client", "client.ClientCommandArguments"),
    SERVER("server.Server", "server.ServerCommandArguments"),
    UNKNOWN(null, "common.CommandArguments");

    fun instance(): EnvironmentClass<*>? {
        return Class.forName(classPath ?: return null).kotlin.objectInstance as? EnvironmentClass<*>
    }

    fun commandArguments(): CommandArguments? {
        return Class.forName(commandArgPath ?: return null).kotlin.primaryConstructor?.call() as? CommandArguments
    }

    companion object {
        fun current(): Environment {
            for (entry in entries) {
                try {
                    Class.forName(entry.classPath, false, Game.javaClassLoader)
                    return entry
                } catch (ignored: Exception) {}
            }
            return UNKNOWN
        }
    }
}
