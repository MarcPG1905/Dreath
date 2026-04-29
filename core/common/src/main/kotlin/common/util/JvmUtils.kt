package common.util

import kotlin.concurrent.thread

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
