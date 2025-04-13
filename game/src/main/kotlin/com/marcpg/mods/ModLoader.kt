package com.marcpg.mods

import com.marcpg.Game
import com.marcpg.dreath.DreathMod
import com.marcpg.dreath.ModInfo
import com.marcpg.log.dreathLogger
import com.marcpg.util.Constants
import com.marcpg.util.config.ReadableJson
import com.marcpg.util.resolveDiscordInvite
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
        val modsDir = Game.DIR.resolve("mods")
        modsDir.createDirectories()
        for (modFile in modsDir.listDirectoryEntries("*.jar")) {
            runCatching {
                loadJar(modFile)
                newlyLoaded++
            }.onFailure {
                Game.LOG.error("Could not load and initialize mod ${modFile.name} due to error.")
                it.printStackTrace()
            }
        }
        return Pair(newlyLoaded, LOADED_MODS.size)
    }

    fun enable() {
        LOADED_MODS.values.forEach { enable(it) }
    }

    fun enable(mod: DreathMod) {
        Game.LOG.fine("Enabling mod ${mod.info.id}...")
        runCatching {
            mod.internalEnable()
        }.onFailure {
            Game.LOG.error("Could not enable mod ${mod.info.id} due to error.")
            it.printStackTrace()
        }
    }

    fun unload() {
        LOADED_MODS.values.forEach { unload(it) }
    }

    fun unload(mod: DreathMod) {
        Game.LOG.fine("Unloading mod ${mod.info.id}...")
        runCatching {
            mod.internalDisable()
        }.onFailure {
            Game.LOG.error("Could not disable and unload mod ${mod.info.id} due to error.")
            it.printStackTrace()
        }
    }

    fun reload() {
        LOADED_MODS.values.forEach { reload(it) }
    }

    fun reload(mod: DreathMod) {
        Game.LOG.fine("Reloading mod ${mod.info.id}...")
        runCatching {
            mod.internalDisable()
            mod.internalReInit()
            mod.internalDisable()
        }.onFailure {
            Game.LOG.error("Could not reload mod ${mod.info.id} due to error.")
            it.printStackTrace()
        }
    }

    fun loaded(): Int = LOADED_MODS.size

    private fun loadJar(modFile: Path) {
        JarFile(modFile.toFile(), true).use { jar ->
            runCatching {
                val info = extractInfo(modFile.toFile(), jar, jar.getJarEntry("dreath-mod.json"))

                if (info.id in LOADED_MODS) {
                    Game.LOG.error("Duplicate ID ${info.id} - already loaded")
                    return
                }

                if (Constants.VERSION < info.dependencies.dreath) {
                    Game.LOG.error("Mod ${info.name} requires Dreath ${info.dependencies.dreath} but ${Constants.VERSION} is provided")
                    return
                }

                val dreathMod = info.main.objectInstance
                if (dreathMod != null) {
                    Game.LOG.fine("Loading mod ${info.name}...")

                    dreathMod.internalInit(dreathLogger(info.id), info)
                    LOADED_MODS[info.id] = dreathMod

                    Game.LOG.fine("Loaded mod ${info.name}.")
                } else {
                    Game.LOG.error("Could not load mod ${info.name}. Main class is either missing or a class instead of an object.")
                }
            }.onFailure {
                Game.LOG.error(it.message ?: "Error loading mod ${modFile.name}.")
                it.printStackTrace()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractInfo(file: File, jar: JarFile, entry: JarEntry?): ModInfo {
        if (entry == null)
            throw IllegalArgumentException("No dreath-mod.json in mod ${file.name}.")

        jar.getInputStream(entry).use { stream -> stream.bufferedReader().use { reader ->
            val settings = ReadableJson(reader.readText())
            runCatching { settings.load() }
                .onFailure { it.printStackTrace() }

            runCatching {
                return@extractInfo ModInfo(
                    settings.getString("id"),
                    settings.getString("name"),
                    settings.getString("version"),
                    settings.getString("description"),
                    ModClassLoader.loadClass(file, settings.getString("main"), settings.getString("id")) as KClass<out DreathMod>,
                    settings.getArray("developers").map { it.jsonPrimitive.content },
                    ModInfo.Contact(
                        URI.create(settings.getString("contact.website", "https://example.com/")),
                        resolveDiscordInvite(settings.getString("contact.discord")) ?: URI.create("https://discord.com/"),
                        settings.getString("contact.email"),
                        URI.create(settings.getString("contact.source", "https://example.com/")),
                        settings.getArray("contact.extra").map { URI.create(it.jsonPrimitive.content) }
                    ),
                    ModInfo.Dependencies(
                        settings.getString("dependencies.dreath", "1.0.0"),
                        settings.getArray("dependencies.external").map { ModInfo.Dependencies.Dependency(
                            it.jsonObject["id"]?.jsonPrimitive?.contentOrNull ?: "",
                            it.jsonObject["min-version"]?.jsonPrimitive?.contentOrNull ?: "",
                            it.jsonObject["required"]?.jsonPrimitive?.booleanOrNull ?: false,
                            enumValueOf<ModInfo.Dependencies.Dependency.LoadOrder>(it.jsonObject["load"]?.jsonPrimitive?.contentOrNull ?: "AUTO")
                        ) }
                    )
                )
            }.onFailure {
                it.printStackTrace()
                throw IllegalArgumentException("Could not parse dreath-mod.json for mod ${file.name}.")
            }
        } }
        throw IllegalArgumentException("Could not read dreath-mod.json for mod ${file.name}.")
    }
}