package common.mods

import com.marcpg.dreath.DreathMod
import com.marcpg.dreath.ModInfo
import com.marcpg.dreath.event.EventBus
import com.marcpg.dreath.event.events.ModDisabledEvent
import com.marcpg.dreath.event.events.ModEnabledEvent
import com.marcpg.dreath.event.events.ModReloadedEvent
import com.marcpg.dreath.log.dreathLogger
import com.marcpg.dreath.log.printStackTrace
import com.marcpg.dreath.log.printStackTraceFine
import com.marcpg.dreath.util.SimpleVersion
import common.Game
import common.util.InternalConstants
import common.util.config.StaticJsonConfigProvider
import common.util.resolveDiscordInvite
import kotlinx.serialization.json.jsonObject
import java.io.File
import java.net.URI
import java.nio.file.Path
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.io.path.createDirectories
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.reflect.KClass

object ModLoader {
    internal val LOADED_MODS = mutableMapOf<String, DreathMod>()

    fun init(): Pair<Int, Int> {
        var newlyLoaded = 0
        runCatching {
            val availableMods = loadAllJars()
            val loadOrder = LoadOrderResolution.resolve(availableMods)

            for (mod in loadOrder) {
                loadMod(mod)
                newlyLoaded++
            }
        }.onFailure {
            Game.LOG.error(it.message, it)
        }
        return Pair(newlyLoaded, LOADED_MODS.size)
    }

    fun enable() {
        LOADED_MODS.values.forEach { mod ->
            mod.logger.fine("Enabling mod ${mod.info.id}...")
            runCatching {
                mod.internalEnable()
                EventBus.post(ModEnabledEvent(mod.info))
            }.onFailure {
                mod.logger.error("Could not enable mod ${mod.info.id} due to error.", it)
            }
        }
    }

    fun unload() {
        LOADED_MODS.values.forEach { mod ->
            mod.logger.fine("Unloading mod ${mod.info.id}...")
            runCatching {
                mod.internalDisable()
                EventBus.post(ModDisabledEvent(mod.info))
            }.onFailure {
                mod.logger.error("Could not disable and unload mod ${mod.info.id} due to error.", it)
            }
        }
    }

    fun reload() {
        LOADED_MODS.values.forEach { reloadMod(it) }
    }

    fun reloadMod(mod: DreathMod) {
        mod.logger.fine("Reloading mod ${mod.info.id}...")
        runCatching {
            mod.internalDisable()
            mod.internalReInit()
            EventBus.post(ModReloadedEvent(mod.info))
        }.onFailure {
            mod.logger.error("Could not reload mod ${mod.info.id} due to error.", it)
        }
    }

    fun loaded(): Int = LOADED_MODS.size

    fun loadAllJars(): Set<ModInfo> {
        val modsDir = Game.DIR.resolve("mods")
        modsDir.createDirectories()

        val mods = mutableSetOf<ModInfo>()
        for (modFile in modsDir.listDirectoryEntries("*.jar")) {
            runCatching {
                mods += loadJar(modFile) ?: return@runCatching
            }.onFailure {
                Game.LOG.error("Could not load mod info for ${modFile.name} due to error: ${it.message}")
                it.printStackTraceFine(Game.LOG)
            }
        }
        return mods
    }

    private fun loadJar(modFile: Path): ModInfo? {
        JarFile(modFile.toFile(), true).use { jar ->
            runCatching {
                val dreathModJsonFile = jar.manifest?.mainAttributes?.getValue("Dreath-Mod-Json")
                return extractInfo(modFile.toFile(), jar, jar.getJarEntry(dreathModJsonFile))
            }.onFailure {
                Game.LOG.error(it.message ?: "Error loading mod ${modFile.name}.", it)
            }
        }
        return null
    }

    private fun loadMod(info: ModInfo) {
        if (info.id in LOADED_MODS) {
            Game.LOG.error("Duplicate ID ${info.id} - already loaded")
            return
        }

        val dreathMod = info.main.objectInstance
        if (dreathMod != null) {
            val logger = dreathLogger(info.id)
            logger.fine("Loading mod ${info.name}...")

            runCatching {
                dreathMod.internalInit(logger, info)
                LOADED_MODS[info.id] = dreathMod

                logger.fine("Loaded mod ${info.name}.")
            }.onFailure {
                Game.LOG.error("Could not load mod ${info.name} due to error in the mod's code.", it)
            }
        } else {
            Game.LOG.error("Could not load mod ${info.name}. Main class is either missing or a class instead of an object.")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractInfo(file: File, jar: JarFile, entry: JarEntry?): ModInfo {
        if (entry == null)
            throw IllegalArgumentException("No dreath-mod.json in mod ${file.name}.")

        jar.getInputStream(entry).use { stream -> stream.bufferedReader().use { reader ->
            runCatching {
                val settings = StaticJsonConfigProvider(InternalConstants.JSON.parseToJsonElement(reader.readText()).jsonObject)
                return@extractInfo ModInfo(
                    settings.getString("id"),
                    settings.getString("name"),
                    SimpleVersion.fromString(settings.getString("version")),
                    settings.getString("description"),
                    ModClassLoader.loadClass(file, settings.getString("main"), settings.getString("id")) as KClass<out DreathMod>,
                    settings.getStringList("developers") ?: listOf(),
                    ModInfo.Contact(
                        URI.create(settings.getString("contact.website", "https://example.com/")),
                        resolveDiscordInvite(settings.getString("contact.discord")) ?: URI.create("https://discord.com/"),
                        settings.getString("contact.email"),
                        URI.create(settings.getString("contact.source", "https://example.com/")),
                        settings.getStringList("contact.extra")?.map { URI.create(it) } ?: listOf()
                    ),
                    ModInfo.Dependencies(
                        SimpleVersion.fromString(settings.getString("dependencies.dreath", "1.0.0")),
                        settings.getMapList("dependencies.external")?.map { ModInfo.Dependencies.Dependency(
                            (it["id"] as? String) ?: "",
                            SimpleVersion.fromString((it["min-version"] as? String) ?: "0.0.0"),
                            (it["required"] as? Boolean) ?: false,
                            enumValueOf<ModInfo.Dependencies.Dependency.LoadOrder>((it["load"] as? String) ?: "AUTO")
                        ) } ?: listOf()
                    )
                )
            }.onFailure {
                it.printStackTrace(Game.LOG)
                throw IllegalArgumentException("Could not parse dreath-mod.json for mod ${file.name}.")
            }
        } }
        throw IllegalArgumentException("Could not read dreath-mod.json for mod ${file.name}.")
    }
}
