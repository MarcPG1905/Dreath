package com.marcpg.mods

import com.marcpg.Game
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.KClass

object ModClassLoader {
    private val CLASS_LOADERS = mutableMapOf<String, ClassLoader>()

    fun loadClass(file: File, className: String, id: String): KClass<out Any> {
        val classLoader = CLASS_LOADERS.getOrPut(id) { URLClassLoader(id, arrayOf(file.toURI().toURL()), Game::class.java.classLoader) }
        return Class.forName(className, true, classLoader).kotlin
    }
}