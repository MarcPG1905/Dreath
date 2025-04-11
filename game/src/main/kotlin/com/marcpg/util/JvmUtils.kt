package com.marcpg.util

import java.io.File
import java.lang.management.ManagementFactory
import kotlin.concurrent.thread
import kotlin.system.exitProcess

fun onShutdownProcess(block: () -> Unit) {
    Runtime.getRuntime().addShutdownHook(Thread(block))
}

fun keepAliveProcess() {
    thread {
        try {
            Thread.sleep(Long.MAX_VALUE)
        } catch (_: InterruptedException) {}
    }
}

fun restartProcess() {
    val javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java"
    val vmArgs = ManagementFactory.getRuntimeMXBean().inputArguments
    val classPath = System.getProperty("java.class.path")
    val mainClass = System.getProperty("sun.java.command").split(" ")[0]
    val mainArgs = System.getProperty("sun.java.command").split(" ").drop(1)

    val command = mutableListOf(javaBin)
    command.addAll(vmArgs)
    command.add("-cp")
    command.add(classPath)
    command.add(mainClass)
    command.addAll(mainArgs)

    ProcessBuilder(command)
        .inheritIO()
        .start()

    exitProcess(0)
}
